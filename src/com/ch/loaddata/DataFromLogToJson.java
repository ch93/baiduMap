package com.ch.loaddata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import com.ch.bean.GPSpoint;
import com.ch.net.HttpNet;
import com.ch.util.MyConstant;

/**
 * 把GPS log 文件转换成 JSON格式文件
 * @author ch
 *
 */
public class DataFromLogToJson {
	
	Logger log = Logger.getLogger(DataFromLogToJson.class);
	
	public void JsonToLocal(String filePath, int user) {
//		String filePath = MyConstant.filePath + "Data/000/Trajectory/20090403011657.plt";
		List<GPSpoint> GPSpoints = fromLogToJson(filePath, user);
		if (GPSpoints == null) {
			System.out.println("数据加载失败");
			//从百度api调用失败的GPS文件记录下来，下次自己在调用
			log.error(filePath);
			return;
		}
		JSONArray jArray = new JSONArray();
		for (GPSpoint gpSpoint : GPSpoints) {
			jArray.put(gpSpoint.toJsonAll());
		}
		BufferedWriter writer = null;
//		String path = MyConstant.filePath + "JsonData/000/Trajectory/20090403011657.json";
//		System.out.println(filePath);
		String path = filePath.replace("Data", "JsonData").replace(".plt", ".json");
//		System.out.println(path);
		File file = new File(path);
        String folderPath = path.substring(0, path.lastIndexOf("\\"));
        File folder =new File(folderPath);
		  //如果文件夹不存在则创建  
		  if  (!folder.exists() && !folder.isDirectory()) {
		      System.out.println("创建目录： " + folderPath);
		      folder.mkdirs();
		  }
        
        if(!file.exists()) { //如果文件不存在，则新建一个
            try {
                file.createNewFile();
                writer = new BufferedWriter(new FileWriter(file));
                //Json 文件写入
                writer.write(jArray.toString());
//                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { 
        	System.out.println("文件已经存在！"+ file.getPath());
        }
        

      
		
	}

	
	public List<GPSpoint> fromLogToJson(String filePath, int user) {
		String url = "http://api.map.baidu.com/geoconv/v1/";
		LoadDataFromLog dataFromLog = new LoadDataFromLog();
		HttpNet httpNet = new HttpNet();
		
		List<GPSpoint> GPSpoints= dataFromLog.readTextFile(filePath, user);
		if (GPSpoints.size() == 0) {
			return null;
		}
//		int arraySize = 1;
//		if (GPSpoints.size()%100 == 0) {
//			arraySize = GPSpoints.size()/100;
//		} else {
//			arraySize = GPSpoints.size()/100 + 1;
//		}
		int arraySize = GPSpoints.size()/100; ////这样子可能还有余数
		if (GPSpoints.size()%100 != 0) {
			arraySize++;
		}
		String coords[] = new String[arraySize];
		int j = 0;
		String temp = "";
		for (int i = 0; i < GPSpoints.size(); i++) {
			GPSpoint gpSpoint = GPSpoints.get(i);
			temp += "" + gpSpoint.getLngt() + "," + gpSpoint.getLat()+";";
			if (i % 100 == 99) {
				coords[j] = temp.substring(0, temp.length()-1);
				j++;
				temp = "";
			}
			if (i  == GPSpoints.size()-1 && (i % 100 != 99)) {
				coords[j] = temp.substring(0, temp.length()-1);
				temp = "";
			}
		}
		for (int i = 0; i < coords.length; i++) {
			//doPost 的参数，做坐标转化
//			System.out.println(string);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ak", "2hHDLHPkjtI66ElRG87t1IBM"));
			params.add(new BasicNameValuePair("output", "json"));
			params.add(new BasicNameValuePair("coords", coords[i]));
			//从百度后天转换坐标，获得Json格式
			String result = httpNet.doPost(url, params);
//			System.out.println(result);
			//网络异常,出现httpCode不为 200，终止
			if (result == null) {
				return null;
			}
			try {
				JSONObject jObject = new JSONObject(result);
				//百度转换后的状态码
				int status = jObject.getInt("status");
				if (status == 0) { //坐标转换成功
					JSONArray jArray = jObject.getJSONArray("result");
					for (int k = 0; k < jArray.length(); k++) {
						JSONObject jObject1 = jArray.getJSONObject(k);
						double lngt = jObject1.getDouble("x");
						double lat = jObject1.getDouble("y");
						GPSpoint point = GPSpoints.get(i*100 + k);
						point.setLat(lat);
						point.setLngt(lngt);
					}
				} else {
					System.out.println("百度转换后的状态码： " + result + " "+filePath);
					return null;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//从百度api调用失败的GPS文件记录下来，下次自己在调用
				log.error(filePath);
				e.printStackTrace();
				
			}
		}
		return GPSpoints;

	}
}
