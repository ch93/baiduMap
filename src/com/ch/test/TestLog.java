package com.ch.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;

import com.ch.bean.FileAtrribute;
import com.ch.dbscan.Dbscan;
import com.ch.dbscan.Point;
import com.ch.dbscan.Utility;
import com.ch.loaddata.DataFromLogToJson;
import com.ch.loaddata.MyDataBase;
import com.ch.module.ExtractStayPointThread;
import com.ch.module.Geocoding;
import com.ch.module.GeocodingThread;
import com.ch.util.MTools;

public class TestLog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		PropertyConfigurator.configure(System.getProperty("user.dir")+"/src/log4j.properties");
//		Logger log = Logger.getLogger(TestLog.class);
//		MTools.SetLogFileName("test");
//		log.error("hi ");
//		DataFromLogToJson data = new DataFromLogToJson();
//		data.JsonToLocal();
		
		
		
//		System.out.println(MTools.calcuDistance(116.318417,39.984702, 116.31066, 39.984656));
		
		
//		System.out.println(MTools.calcuDistance(116.33114309401, 39.991767378425, 116.3233315091, 39.991825424245));
//		double ss = Math.abs(39747.5723032407 - 39747.5723611111);
//		
//		System.out.println(40951%7);
		
		
		
//		ArrayList<FileAtrribute> filelist = MTools.getFiles("JsonData",0,1);
//		//总文件数
//		System.out.println(filelist.size());
//		// 设定要启动的工作线程数为 5 个  
//        int threadCount = 5;
//        List[] taskListPerThread = MTools.distributeTasks(filelist, threadCount);
//		System.out.println(116);
		List<String> filelist = new ArrayList<String>();
		for (int userid = 100; userid < 182; userid++) {
			String tempFileName = "";
			if (userid < 10) {
				tempFileName = "00" + userid;
			} else if (userid < 100) {
				tempFileName = "0" + userid;
			} else if (userid < 178){
				tempFileName = "" + userid;
			} else {
				tempFileName = "177";
			}
			String path = "D:\\Geolife Trajectories 1.3\\" + "stayPointData\\" + tempFileName
			+ "\\stayPoint.json";
			filelist.add(path);
	   }
		// 设定要启动的工作线程数为 5 个  
        int threadCount = 5;
        List[] taskListPerThread = MTools.distributeTasks(filelist, threadCount);
        for (int i = 0; i < taskListPerThread.length; i++) {
        	@SuppressWarnings("unchecked")
			List<Object> fileL= (ArrayList<Object>) taskListPerThread[i];
        	GeocodingThread mTread = new GeocodingThread(fileL, i);
        	mTread.start();
        }

   }
}

