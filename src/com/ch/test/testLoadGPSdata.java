package com.ch.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ch.bean.FileAtrribute;
import com.ch.bean.GPSpoint;
import com.ch.loaddata.DataFromLogToJson;
import com.ch.loaddata.LoadDataFromLog;
import com.ch.loaddata.MyDataBase;
import com.ch.module.ExtractStayPointThread;
import com.ch.module.ExtractionStayPoint;
import com.ch.module.TransformBDCoordinatesThread;
import com.ch.util.MTools;
import com.ch.util.MyConstant;

public class testLoadGPSdata {

	static LoadDataFromLog gps = new LoadDataFromLog();
	static DataFromLogToJson json = new DataFromLogToJson();
	
	static int currentN = 0;
	
	static int totalN = 1;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final long startTime=System.currentTimeMillis();   //获取开始时间

		
//		Timer timer = new Timer();  
//        timer.schedule(new TimerTask() {  
//            public void run() {  
//            	long endTime=System.currentTimeMillis(); //获取结束时间 
//            	float tempm = (float)currentN/totalN;
//                System.out.println("当前进度： " + (int)(tempm*100) + "%" + "  程序运行： "+(endTime-startTime)/(1000*60)+"min");  
//            }
//        }, 1000, 1000*60);// 设定指定的时间time,此处为2000毫秒  
        
        
//		loadData();
		
//		CountData();
		

		
//		transformGPSToJson();
        
//		transformGPSToJsonAll();
        
//      extractionStayPoint();
      
//      testCollectStayPoint();
		
//		emerger();
		
		countStayPoint();
		
//		timer.cancel();
		
//		String path1 = "C:\\Users\\ch\\Desktop\\论文\\data\\Geolife Trajectories 1.2\\testData\\000\\Trajectory\\20081023025304.plt";
//		List<GPSpoint> gPSpoints = gps.readTextFile(path1, 0);
//		dataBase.insertGPSdata(gPSpoints);
        
		long endTime=System.currentTimeMillis(); //获取结束时间 
		System.out.println("程序一共运行时间： "+(endTime-startTime)/(1000*60)+"min");
	}
	
	/****************************************function************************************/
	/**
	 * 单个文件停留点提取
	 */
	public static void testCollectStayPoint() {
		ExtractionStayPoint extractionStayPoint = new ExtractionStayPoint();
		String filePath = "D:\\Geolife Trajectories 1.3\\Data\\085\\Trajectory\\20081115003759.plt";
		extractionStayPoint.collectStayPoint(filePath, 85);
		System.out.println("OK");
	}
	
	/**
	 * 计算停留点个数
	 */
	public static void countStayPoint() {
		ExtractionStayPoint stayPoint = new ExtractionStayPoint();
		ArrayList<FileAtrribute> filelist = new ArrayList<FileAtrribute>(); 
		for (int i = 0; i < 182; i++) {
			String tempFileName = "";
			if (i < 10) {
				tempFileName = "00" + i;
			} else if (i < 100) {
				tempFileName = "0" + i;
			} else {
				tempFileName = "" + i;
			}
			String path = MyConstant.filePath + "stayPointData/" + tempFileName +"/stayPoint.json";
			FileAtrribute file = new FileAtrribute(i, path);
			filelist.add(file);
		}
		System.out.println(stayPoint.countStayPoint(filelist));
		System.out.println("ok");
		
	}
	/**
	 * 合并停留点
	 */
	public static void emerger() {
		ExtractionStayPoint stayPoint = new ExtractionStayPoint();
		for (int i = 0; i < 182; i++) {
			ArrayList<FileAtrribute> filelist = MTools.getFiles("stayPointData",i,i + 1);
			if (filelist.isEmpty()) {
				continue ;
			}
			stayPoint.emerger(filelist);
			System.out.println("合并完成");
		}

	}
	
	/**
	 * 提取停留点
	 */
	public static void extractionStayPoint() {
		// 0 - 182
		ArrayList<FileAtrribute> filelist = MTools.getFiles("Data",0,182);
		//总文件数
		totalN = filelist.size();
		System.out.println("plt num " + totalN);
		// 设定要启动的工作线程数为 4 个  
        int threadCount = 3;
        List[] taskListPerThread = MTools.distributeTasks(filelist, threadCount);
        for (int i = 0; i < taskListPerThread.length; i++) {
        	@SuppressWarnings("unchecked")
			ArrayList<FileAtrribute> fileL= (ArrayList<FileAtrribute>) taskListPerThread[i];
        	ExtractStayPointThread mTread = new ExtractStayPointThread(fileL, i);
        	mTread.start();
        }
	}
	
	
	/**
	 * 把原始GPS坐标转换成百度坐标(单个)
	 */
	public static void transformGPSToJson() {
		String array[] = new String[]{
"D:\\Geolife Trajectories 1.3\\Data\\020\\Trajectory\\20110911000506.plt"
		};
		for (int i = 0; i < array.length; i++) {
			json.JsonToLocal(array[i], 0);
		}
//		String Filepath = "C:\\Users\\ch\\Desktop\\paper\\data\\Geolife Trajectories 1.2\\Data\\003\\Trajectory\\20090525093647.plt";
	}
	
	/**
	 * 把原始GPS坐标转换成百度坐标
	 */
	private static void transformGPSToJsonAll() {
		
		ArrayList<FileAtrribute> filelist = MTools.getFiles("Data",0,182);
		//总文件数
		totalN = filelist.size();
		System.out.println("plt num " + totalN);
		// 设定要启动的工作线程数为 5 个  
        int threadCount = 10;
        List[] taskListPerThread = MTools.distributeTasks(filelist, threadCount);
        for (int i = 0; i < taskListPerThread.length; i++) {
        	@SuppressWarnings("unchecked")
			ArrayList<FileAtrribute> fileL= (ArrayList<FileAtrribute>) taskListPerThread[i];
        	TransformBDCoordinatesThread mTread = new TransformBDCoordinatesThread(fileL, i);
        	mTread.start();
        }
        
	}
	



	/**
	 * 统计整个数据库的记录数
	 */
	private static void CountData() {
		// TODO Auto-generated method stub
		MyDataBase dataBase = new MyDataBase();
		int count = dataBase.countcountGPSInfoTatal();
		System.out.println("记录数： " + count);
	}

	/**
	 * 从plt中读取数据，并写入到mysql数据库中
	 */
	private static void loadData() {
		// TODO Auto-generated method stub
		
		MyDataBase dataBase = new MyDataBase();
		String path = MyConstant.filePath + "Data";
		gps.getFiles(path);
		gps.fileCount();
		ArrayList<FileAtrribute> filelist = gps.getFilelist();
		
		TimeThread myThread = new TimeThread();
		myThread.start();
		
		myThread.setTotalNum(filelist.size());
		for (int i=0; i < filelist.size(); i++) {
			FileAtrribute file = filelist.get(i);
			List<GPSpoint> gPSpoints = gps.readTextFile(file.getFilePath(), file.getFileNum());
			if (gPSpoints.size() == 0) {
				continue;
			} else {
				dataBase.insertGPSdata(gPSpoints);
			}
			myThread.setI(i);
		}
		//关掉线程
		myThread.setFlag(true);

	}

	
	

	
}

