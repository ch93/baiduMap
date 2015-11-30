package com.ch.dbscan;

import java.io.IOException;
import java.util.*;

import com.ch.util.MyConstant;
/**
 * DBscan算法实现
 * @author 
 *
 */
public class Dbscan {
	private final static int e = 15;// ε半径
	private final static int minp = 1;// 密度阈值
	public  List<Point> pointsList = new ArrayList<Point>();// 存储原始样本点
	public  List<List<Point>> resultList = new ArrayList<List<Point>>();// 存储最后的聚类结果

	public  void applyDbscan(String path) throws IOException {
		// String txtPath =
		// "D:\\dev\\workspace_javaWeb\\baiduMap\\testData\\points.txt";

		pointsList = Utility.getPointsList(path);
		for (int index = 0; index < pointsList.size(); ++index) {
			List<Point> tmpLst = new ArrayList<Point>();
			Point p = pointsList.get(index);
			if (p.isClassed())
				continue;
			tmpLst = Utility.isKeyPoint(pointsList, p, e, minp);
			if (tmpLst != null) {
				resultList.add(tmpLst);
			}
		}
		int length = resultList.size();
		for (int i = 0; i < length; ++i) {
			for (int j = 0; j < length; ++j) {
				if (i != j) {
					if (Utility.mergeList(resultList.get(i), resultList.get(j))) {
						resultList.get(j).clear();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
//		try {
//			// 调用DBSCAN的实现算法
//			String path = "D:\\Geolife Trajectories 1.3\\" + "stayPointData\\" + "005"
//					+ "\\stayPoint.json";
//			applyDbscan(path);
//			Utility.display(resultList);
//			Utility.saveResult(resultList, path.replace("stayPoint.json", "clusteredStayPoint.json"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		for (int userid = 0; userid < 11; userid++) {
//			String tempFileName = "";
//			if (userid < 10) {
//				tempFileName = "00" + userid;
//			} else if (userid < 100) {
//				tempFileName = "0" + userid;
//			} else if (userid < 178){
//				tempFileName = "" + userid;
//			} else {
//				tempFileName = "177";
//			}
//			try {
//				// 调用DBSCAN的实现算法
//				String path = "D:\\Geolife Trajectories 1.3\\" + "stayPointData\\" + tempFileName
//						+ "\\stayPoint.json";
//				applyDbscan(path);
//				Utility.display(resultList);
//				Utility.saveResult(resultList, path.replace("stayPoint.json", "clusteredStayPoint.json"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}

	}

}
