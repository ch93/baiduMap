package com.ch.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ch.bean.FileAtrribute;
import com.ch.bean.GPSpoint;
import com.ch.bean.StayPoint;
import com.ch.loaddata.LoadDataFromLog;
import com.ch.util.MTools;
import com.ch.util.MyConstant;

/**
 * 活动点（停留点）检测 
 * @author ch
 *
 */
public class ExtractionStayPoint {

	//原始GPS点链表
	private List<GPSpoint> list = null;
	//停留点链表
	private List<StayPoint> listS = null;
	
	public ExtractionStayPoint() {
		list = new LinkedList<GPSpoint>();
		listS = new ArrayList<StayPoint>();
	}
	/**
	 * 生成链表
	 * @return
	 */
	public boolean generateList(String filePath) {
		//清除List中的元素
		list.clear();
		String result = MTools.readFile(filePath);
		if ("".equals(result)) {
			return false;
		}
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject object = jArray.getJSONObject(i);
				GPSpoint point = MTools.jsonToGPSpointBean(object);
				if (point != null) {
					list.add(point);
				} else {
					System.out.println("json 转换 bean 对象出问题");
				}
			}
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 生成链表 
	 * @param filePath 文件路径
	 * @param user 第几个用户
	 * @return 生成成功否
	 */
	public boolean generateList(String filePath, int user) {
//		List<GPSpoint> gPSpoints = new ArrayList<GPSpoint>();
		list.clear();
		File file = new File(filePath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			 int line = 1;
			 int userid = -1;
			 double lat = 0;
			 double lngt = 0;
			 float height = 0;
			 double itime = 0;
			 Timestamp gtime = Timestamp.valueOf("1970-01-01 00:00:00");
			
			while((tempString = reader.readLine()) != null) {
				
				if (line > 6) {
					String[] tempArray = tempString.split(",");
					if (tempArray.length > 0) {
						lat =  Double.parseDouble(tempArray[0]);
						lngt = Double.parseDouble(tempArray[1]);
						height = (float)Double.parseDouble(tempArray[3]);
						itime = Double.parseDouble(tempArray[4]);
						gtime = Timestamp.valueOf(tempArray[5]+" "+tempArray[6]);
//						GPSpoint point = new GPSpoint(user, lat, lngt, height, gtime);
						GPSpoint point = new GPSpoint(user, lat, lngt, height, gtime, itime);
						list.add(point);
					}
				}
				line++;
			} 
			return true;
		} catch (IllegalArgumentException e) {
			Logger log = Logger.getLogger(LoadDataFromLog.class);
			log.error("IllegalArgumentException 显示 " + filePath);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger log = Logger.getLogger(LoadDataFromLog.class);
			log.error("IOException 显示 " + filePath);
		}
		return false;
	}
	
	/**
	 * 检测停留点
	 */
	public void detectStayPoint(int DistanceThreshold, int TimeThreshold) {
		//gps点个数
		int pointNum = list.size();
		int i = 0;
		int j = 0;
		
		int dffD = 0;  //距离差
		int dffT = 0;  //时间差
		//建议使用for 不容易掉 i++
		while(i < pointNum - 1) {
			j = i + 1;
			while(j < pointNum) {
				//计算距离差
				dffD = (int)MTools.calcuDistance(list.get(i), list.get(j));
				if (dffD > DistanceThreshold || j == pointNum - 1) {
					long diff = list.get(j).getGtime().getTime() - list.get(i).getGtime().getTime();
					dffT = (int)(diff/(1000*60));  //单位为分钟
					if (dffT > TimeThreshold) {
						double lat = computeMeanLat(i, j);
						double lngt = computeMeanLngt(i, j);
						Timestamp arvT = list.get(i).getGtime();
						Timestamp levT = list.get(j).getGtime();
						double iArvT = list.get(i).getItime();
						double iLevT = list.get(j).getItime();
//						StayPoint point = new StayPoint(lat, lngt, arvT, levT);
						StayPoint point = new StayPoint(lat, lngt, arvT, levT, iArvT, iLevT);
						listS.add(point);
					}
					//第i个节点 转到第j个节点 
					i = j - 1;
					break;
				}
				j++;
			}
			i++;
		}
	}
	
//	
//	public void detectStayPoint(int DistanceThreshold, int TimeThreshold) {
//		//gps点个数
//		int pointNum = list.size();
//		int i = 0;
//		int j = 0;
//		
//		int dffD = 0;  //距离差
//		int dffT = 0;  //时间差
//		//建议使用for 不容易掉 i++
//		while(i < pointNum - 1) {
//			j = i + 1;
//			while(j < pointNum) {
//				//计算距离差
//				dffD = (int)MTools.calcuDistance(list.get(i), list.get(j));
//				if (dffD > DistanceThreshold ) {
//					if (i == j - 1) {
//						i = j - 1;
//						break;
//					}
//					long diff = list.get(j - 1).getGtime().getTime() - list.get(i).getGtime().getTime();
//					dffT = (int)(diff/(1000*60));  //单位为分钟
//					if (dffT > TimeThreshold ) {
//						double lat = computeMeanLat(i, j - 1);
//						double lngt = computeMeanLngt(i, j - 1);
//						Timestamp arvT = list.get(i).getGtime();
//						Timestamp levT = list.get(j - 1).getGtime();
//						StayPoint point = new StayPoint(lat, lngt, arvT, levT);
//						listS.add(point);
//					}
//					//第i个节点 转到第j个节点 
//					i = j - 1;
//					break;
//				}
//				j++;
//			}
//			i++;
//		}
//	}
	
	
	//计算纬度的平均值
	private double computeMeanLat(int p1, int p2) {
		double total = 0;
		for (int i = p1; i <= p2; i++) {
			 total += list.get(i).getLat();
		}
		return total/(p2 - p1 + 1); 
	}
	//计算经度的平均值
	private double computeMeanLngt(int p1, int p2) {
		double total = 0;
		for (int i = p1; i <= p2; i++) {
			 total += list.get(i).getLngt();
		}
		return total/(p2 - p1 + 1); 
	}
	
	/**
	 * 保存停留点链表
	 */
	public void saveStayPoint(String saveFilePath) {
		int pointNum = listS.size();
		JSONArray jArray = new JSONArray();
		for (int i = 0; i < pointNum; i++) {
			StayPoint point = listS.get(i);
			jArray.put(point.toJson());
		}
		MTools.writeFile(saveFilePath, jArray.toString());
	}
	
	/**
	 * 保存停留点链表
	 */
	public void saveStayPoint(String saveFilePath, int userid) {
		int pointNum = listS.size();
		if (pointNum > 0) {
			JSONArray jArray = new JSONArray();
			JSONObject jObject = new JSONObject();
			try {
				for (int i = 0; i < pointNum; i++) {
					StayPoint point = listS.get(i);
					jArray.put(point.toJson());
				}
				jObject.put("userid", userid);
				jObject.put("stayPoint", jArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MTools.writeFile(saveFilePath, jObject.toString());
		} else {
			Logger log = Logger.getLogger(ExtractionStayPoint.class);
			MTools.SetLogFileName("NoStayPont");
			log.error("GPS文件中没有停留点  " + saveFilePath.replace("stayPointData", "Data"));
//			System.out.println("");
		}

	}
	//
	private void clearAll() {
		list.clear();
		listS.clear();
	}
	
	private void clear() {
//		list.clear();
		listS.clear();
	}
	/**
	 * 组合操作
	 */
	public void collectStayPoint(String filePath) {
		String saveFilePath = filePath.replace("JsonData", "stayPointData");
		if (MTools.isExistTheFile(saveFilePath)) {
			return;
		}
		generateList(filePath);
		detectStayPoint(MyConstant.DistanceThreshold, MyConstant.TimeThreshold);
		saveStayPoint(saveFilePath);
		clearAll();
	}
	
	/**
	 * 组合操作
	 */
	public void collectStayPoint(String filePath, int userid) {
		String saveFilePath = filePath.replace("Data", "stayPointData");
		if (MTools.isExistTheFile(saveFilePath)) {
			return;
		}
		//generateList(filePath);
		generateList(filePath, userid);
		detectStayPoint(MyConstant.DistanceThreshold, MyConstant.TimeThreshold);
//		String saveFilePath = filePath.replace("JsonData", "stayPointData");
		saveStayPoint(saveFilePath, userid);
		clearAll();
	}
	
	/**
	 * 组合操作
	 */
	public void collectStayPoint(String filePath, int userid, String savaFileName) {
		String saveFilePath = filePath.replace("JsonData", savaFileName);
		if (MTools.isExistTheFile(saveFilePath)) {
			return;
		}
		generateList(filePath);
		detectStayPoint(MyConstant.DistanceThreshold, MyConstant.TimeThreshold);
//		String saveFilePath = filePath.replace("JsonData", "stayPointData");
		saveStayPoint(saveFilePath, userid);
		clearAll();
	}
	
	/**
	 * 合并已经提取的各个停留点json文件
	 * @param filelist
	 */
	public void emerger(ArrayList<FileAtrribute> filelist) {
		JSONObject stayPointObject = new JSONObject();
		JSONArray stayPoint = new JSONArray();
		for (int i = 0; i < filelist.size(); i++) {
			FileAtrribute file = filelist.get(i);
			String result = MTools.readFile(file.getFilePath());
			if ("".equals(result)) {
				
			} else {
				try {
					JSONObject jObject = new JSONObject(result);
					JSONArray jArray = jObject.getJSONArray("stayPoint");
					for (int j = 0; j < jArray.length(); j++) {
						stayPoint.put(jArray.getJSONObject(j));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			stayPointObject.put("userid", filelist.get(0).getFileNum());
			stayPointObject.put("stayPoint", stayPoint);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String path = filelist.get(0).getFilePath();
		path = path.substring(0, path.lastIndexOf("\\"));
		path = path.substring(0, path.lastIndexOf("\\"));
		path = path + "\\stayPoint.json";
		MTools.writeFile(path, stayPointObject.toString());
		
	}
	
	/**
	 * 统计停留点的个数
	 * @param filelist
	 * @return
	 */
	public int countStayPoint(ArrayList<FileAtrribute> filelist) {
		JSONArray stayPoint = new JSONArray();
		for (int i = 0; i < filelist.size(); i++) {
			FileAtrribute file = filelist.get(i);
			String result = MTools.readFile(file.getFilePath());
			if ("".equals(result)) {
				
			} else {
				try {
					JSONObject jObject = new JSONObject(result);
					JSONArray jArray = jObject.getJSONArray("stayPoint");
					//显示每一个用户的停留点
//					System.out.println("id: " + file.getFileNum() + "  Num: " + jArray.length());
					System.out.println(jArray.length());
					for (int j = 0; j < jArray.length(); j++) {
						stayPoint.put(jArray.getJSONObject(j));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return stayPoint.length();
		
	}
	
	
}
