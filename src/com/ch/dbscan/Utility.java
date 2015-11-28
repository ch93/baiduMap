package com.ch.dbscan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ch.util.MTools;
import com.ch.util.MyConstant;

public final class Utility {
	// 计算两点之间的距离
	public static double getDistance(Point p, Point q) {
//		double dx = p.getX() - q.getX();
//		double dy = p.getY() - q.getY();
		//double distance = Math.sqrt(dx * dx + dy * dy);
		//球面距离
		double distance = MTools.calcuDistance(p.getLngt(), p.getLat(), q.getLngt(), q.getLat());
		return distance;
	}

	// 检测p点是不是核心点，tmpLst存储核心点的直达点
	public static List<Point> isKeyPoint(List<Point> lst, Point p, int e, int minp) {
		int count = 0;
		List<Point> tmpLst = new ArrayList<Point>();
		for (Iterator<Point> it = lst.iterator(); it.hasNext();) {
			Point q = it.next();
			if (getDistance(p, q) <= e) {
				++count;
				if (!tmpLst.contains(q)) {
					tmpLst.add(q);
				}
			}
		}
		if (count >= minp) {
			p.setKey(true);
			return tmpLst;
		}
		return null;
	}

	// 合并两个链表，前提是b中的核心点包含在a中
	public static boolean mergeList(List<Point> a, List<Point> b) {
		boolean merge = false;
		if (a == null || b == null) {
			return false;
		}
		for (int index = 0; index < b.size(); ++index) {
			Point p = b.get(index);
			if (p.isKey() && a.contains(p)) {
				merge = true;
				break;
			}
		}
		if (merge) {
			for (int index = 0; index < b.size(); ++index) {
				if (!a.contains(b.get(index))) {
					a.add(b.get(index));
				}
			}
		}
		return merge;
	}

	// 获取文本中的样本点集合
	public static List<Point> getPointsList(String filePath) throws IOException {
		List<Point> lst = new ArrayList<Point>();
		// String txtPath =
		// "D:\\dev\\workspace_javaWeb\\baiduMap\\testData\\points.txt";
//		BufferedReader br = new BufferedReader(new FileReader(filePath));
//		String str = "";
//		while ((str = br.readLine()) != null && str != "") {
//			lst.add(new Point(str));
//		}
//		br.close();
		String result = MTools.readFile(filePath);
		try {
			JSONObject jObject = new JSONObject(result);
			int userid = jObject.getInt("userid");
			JSONArray jArray = jObject.getJSONArray("stayPoint");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jsonObject = jArray.getJSONObject(i);
				double lat = jsonObject.getDouble("lat");
				double lngt = jsonObject.getDouble("lngt");
				Timestamp arvT = Timestamp.valueOf(jsonObject.getString("arvT"));
				Timestamp levT = Timestamp.valueOf(jsonObject.getString("levT"));
				double iArvT = jsonObject.getDouble("iArvT");
				double iLevT = jsonObject.getDouble("iLevT");
				lst.add(new Point(userid, lat, lngt, arvT, levT, iArvT, iLevT));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lst;
	}

	// 显示聚类的结果
	public static void display(List<List<Point>> resultList) {
		int index = 1;
		for (Iterator<List<Point>> it = resultList.iterator(); it.hasNext();) {
			List<Point> lst = it.next();
			if (lst.isEmpty()) {
				continue;
			}
			System.out.println("-----第" + index + "个聚类-----");
			for (Iterator<Point> it1 = lst.iterator(); it1.hasNext();) {
				Point p = it1.next();
				System.out.println(p.print());
			}
			index++;
		}
	}
	/**
	 * 保存聚类后的停留点
	 * @param resultList
	 * @param saveFilePath
	 */
	public static void saveResult(List<List<Point>> resultList, String saveFilePath) {
		if (resultList.size() < 1) {
			return;
		}

		JSONArray jArray = new JSONArray();
		int userid = 0;
		for (Iterator<List<Point>> it = resultList.iterator(); it.hasNext();) {
			List<Point> lst = it.next();
			if (lst.isEmpty()) {
				continue;
			}
			userid = lst.get(0).getUserid();
			for (Iterator<Point> it1 = lst.iterator(); it1.hasNext();) {
				Point p = it1.next();
//				System.out.println(p.print());
				jArray.put(p.toJson());
			}
		}
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("userid", userid);
			jObject.put("stayPoint", jArray);
			//保存文件
			MTools.writeFile(saveFilePath, jObject.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
