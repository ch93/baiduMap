package com.ch.dbscan;

import java.io.IOException;
import java.util.*;

import com.ch.util.MyConstant;

public class Dbscan {
	private final static int e = 200;// ε半径
	private final static int minp = 5;// 密度阈值
	private static List<Point> pointsList = new ArrayList<Point>();// 存储原始样本点
	private static List<List<Point>> resultList = new ArrayList<List<Point>>();// 存储最后的聚类结果

	private static void applyDbscan(String path) throws IOException {
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
		try {
			// 调用DBSCAN的实现算法
			String path = "D:\\Geolife Trajectories 1.3\\" + "stayPointData\\" + "005"
					+ "\\stayPoint.json";
			applyDbscan(path);
			Utility.display(resultList);
			Utility.saveResult(resultList, path.replace("stayPoint.json", "clusteredStayPoint.json"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
