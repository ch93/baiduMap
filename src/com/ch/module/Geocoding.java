package com.ch.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ch.bean.GPSpoint;
import com.ch.dbscan.Point;
import com.ch.loaddata.LoadDataFromLog;
import com.ch.net.HttpNet;
import com.ch.util.MyConstant;

/**
 * @author: ch
 * @date:2015-12-1 下午10:09:16
 * 
 */
public class Geocoding {
     
	/**
	 * 获取地址
	 * @param lat 纬度
	 * @param lngt 经度
	 */
	private String getAddress(double lat, double lngt) {
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ak", MyConstant.MyBaiduKey));
		params.add(new BasicNameValuePair("location", ""+lat+","+lngt));
		params.add(new BasicNameValuePair("output", "json"));
		params.add(new BasicNameValuePair("pois", "1"));
		HttpNet httpNet = new HttpNet();
		String url = "http://api.map.baidu.com/geocoder/v2/";
		String result1 = httpNet.doPost(url, params);
		try {
			JSONObject jObject = new JSONObject(result1);
			//百度转换后的状态码
			int status = jObject.getInt("status");
			if (status == 0) { //坐标转换成功
				JSONArray jArray = jObject.getJSONObject("result").getJSONArray("pois");
				if (jArray.length() > 0) {
					JSONObject jObject1 = jArray.getJSONObject(0);
					String poiType = jObject1.getString("poiType");
					String tag = jObject1.getString("tag");
					if (!"".equals(poiType) && !"".equals(tag)) {
						result = poiType+"|"+tag;
					} else {
						//待提高
						
					}
//					System.out.println(tag);
					return result;
				}
			} else {
				System.out.println("百度转换后的状态码： " + result1 + " "+httpNet.getURLPath());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//从百度api调用失败的GPS文件记录下来，下次自己在调用
			Logger log = Logger.getLogger(Geocoding.class);
			log.error(httpNet.getURLPath());
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * @param resultList
	 */
	public List<List<Point>> lableThePOI(List<List<Point>> resultList) {
		int index = 1;
		for (Iterator<List<Point>> it = resultList.iterator(); it.hasNext();) {
			List<Point> lst = it.next();
			if (lst.isEmpty()) {
				continue;
			}
			String result = null;
			if (lst.size() < 1) {
				System.out.println("there is no data in list");
			} else if(lst.size() < 2) {
				result = getAddress(lst.get(0).getLat(), lst.get(0).getLngt());
			} else {
				double[] averge = calAverage(lst);
				result = getAddress(averge[0], averge[1]);
			}
			
			System.out.println("-----第" + index + "个聚类-----");
			for (Iterator<Point> it1 = lst.iterator(); it1.hasNext();) {
				Point p = it1.next();
				p.setClusterID(index);
				if (result.contains("|")) {
					String[] poiType = result.split("\\|");
					System.out.println(result);
					p.setPoiType(poiType[0]);
					p.setTag(poiType[1]);
				}
//				System.out.println(p.print());
			}
			index++;
		}
		return resultList;
	}
	
	/**
	 * 计算点集的平均值
	 * @param points 点集
	 * @return 经纬度的平均值
	 */
	private double[] calAverage(List<Point>  points) {
		double[] averge = new double[2];
		double lat = 0;
		double lngt = 0;
		int size = points.size();
		for (int i = 0; i < size; i++) {
			Point point = points.get(i);
			lat += point.getLat();
			lngt += point.getLngt();
		}
		averge[0] = lat/size;
		averge[1] = lngt/size;
		return averge;
	}
	
	
	
	public static void main(String[] args) {
		Geocoding geocoding = new Geocoding();
		geocoding.getAddress(39.908927209125515, 116.4053922395437);
		
	}
}
