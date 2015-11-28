package com.ch.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.json.JSONException;
import org.json.JSONObject;

import com.ch.bean.FileAtrribute;
import com.ch.bean.GPSpoint;
import com.ch.bean.StayPoint;

/**
 * 一些通用的工具
 * @author ch
 *
 */
public class MTools {
	
	//6370.99681   6378.137
	private static double EARTH_RADIUS = 6370.99681;  //地球半径  
	/**
	 * 将角度换算为弧度
	 * @param degrees
	 * @return
	 */
	private static double ConvertDegreesToRadians(double degrees){
		return degrees * Math.PI / 180;
	}

    //将用角度表示的角转换为近似相等的用弧度表示的角 Math.toRadians  
    private static double rad(double d){  
        return d * Math.PI / 180.0;  
    }  
  
    /** 
     * 谷歌地图计算两个坐标点的距离 
     * @param lng1  经度1 
     * @param lat1  纬度1 
     * @param lng2  经度2 
     * @param lat2  纬度2 
     * @return 距离（米） 
     */
    public static double calcuDistance(double lng1, double lat1, double lng2, double lat2) {  
        double radLat1 = Math.toRadians(lat1);  
        double radLat2 = Math.toRadians(lat2);  
        double a = radLat1 - radLat2;  
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);  
        //根据  Haversine formula
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +   
         Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
        s = s * EARTH_RADIUS;  
        s = Math.abs(s * 1000);
        BigDecimal bb = new BigDecimal(s);
        return bb.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }  
    
    public static double calcuDistance(GPSpoint point1, GPSpoint point2){  
    	return calcuDistance( point1.getLngt(), point1.getLat(), point2.getLngt(), point2.getLat());
    }  
    
    /**
     * 读取文本
     * @param filePath 路径
     * @return 本文内容
     */
    public  static String readFile(String filePath) {
    	String result = "";
    	File file = new File(filePath);
    	if (file.exists()) { //判断文件是否存在
        	try {
        		BufferedReader reader = new BufferedReader(new FileReader(file));
        		String tempString = null;
        		while ((tempString = reader.readLine()) != null) {
        			result += tempString;
        		}
        	} catch (FileNotFoundException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
		} else {
			System.out.println("there is no file | MTools.readFile");
		}
    	return result;
    }
    
    /**
     * 写文件
     * @param filePath
     * @return
     */
    public  static void writeFile(String path, String content) {
    	
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
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                // 文件写入
                writer.write(content);
//                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { 
        	System.out.println("文件已经存在！"+ file.getPath());
        }
    }
    
    /**
     * 把JSON转化为GPSpointbean对象
     * @param json
     * @return
     */
    public static GPSpoint jsonToGPSpointBean(JSONObject json){
    	try {
			int userid = json.getInt("userid");
			double lat = json.getDouble("lat");
			double lngt = json.getDouble("lngt");
			float height = (float)json.getDouble("height");
			Timestamp gtime = Timestamp.valueOf(json.getString("gtime"));
			GPSpoint gps = new GPSpoint(userid, lat, lngt, height, gtime);
			return gps;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    	
    }
    /**
     * 把JSON转化为StayPointbean对象
     * @param json
     * @return
     */
    public static StayPoint jsonToStayPointBean(JSONObject json){
    	try {
//			int userid = json.getInt("userid");
			double lat = json.getDouble("lat");
			double lngt = json.getDouble("lngt");
			Timestamp arvT = Timestamp.valueOf(json.getString("arvT"));
			Timestamp levT = Timestamp.valueOf(json.getString("levT"));
			StayPoint point = new StayPoint(lat, lngt, arvT, levT);
			return point;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    	
    }
    
    
	 /*
	  * 通过递归得到某一路径下所有的目录及其文件
	  */
	public static ArrayList<FileAtrribute> getFiles(String fileName, int startNum, int endNum) {
		//文件集合
		ArrayList<FileAtrribute> filelist = new ArrayList<FileAtrribute>();
		String tempFileName = "";
		for (int i = startNum; i < endNum; i++) {  //一共有178个文件夹
			if (i < 10) {
				tempFileName = "00" + i;
			} else if (i < 100) {
				tempFileName = "0" + i;
			} else {
				tempFileName = "" + i;
			}
			String filePath = MyConstant.filePath + fileName +"/" + tempFileName +"/Trajectory";
			File root = new File(filePath);
			File[] files = root.listFiles();
			//java.lang.NullPointerException
			if (files != null) {
				for (File file : files) {
					String filepath = file.getAbsolutePath();
//					System.out.println("显示 " + filePath + " 下所有子目录 " + filepath);
					FileAtrribute fileAtrribute = new FileAtrribute(i, filepath);
					filelist.add(fileAtrribute);
				}
			}
		}
		return filelist;
	}
    /**
     * 动态修改log文件名
     * @param filename
     * @return 
     */
	 public static void SetLogFileName(String filename){
		 RollingFileAppender appender = (RollingFileAppender)Logger.getRootLogger().getAppender("logfile");
		 appender.setFile( "log/"+filename+".log");//动态地修改这个文件名 
		 appender.activateOptions();
	 }
	 
	 
	    /** 
	     * 把 List 中的任务分配给每个线程，先平均分配，剩于的依次附加给前面的线程 返回的数组有多少个元素 (List) 就表明将启动多少个工作线程 
	     *  
	     * @param taskList 
	     *            待分派的任务列表 
	     * @param threadCount 
	     *            线程数 
	     * @return 列表的数组，每个元素中存有该线程要执行的任务列表 
	     */  
	    @SuppressWarnings("unchecked")  
	    public static List[] distributeTasks(List taskList, int threadCount) {  
	        // 每个线程至少要执行的任务数,假如不为零则表示每个线程都会分配到任务  
	        int minTaskCount = taskList.size() / threadCount;  
	        // 平均分配后还剩下的任务数，不为零则还有任务依个附加到前面的线程中  
	        int remainTaskCount = taskList.size() % threadCount;  
	        // 实际要启动的线程数,如果工作线程比任务还多  
	        // 自然只需要启动与任务相同个数的工作线程，一对一的执行  
	        // 毕竟不打算实现了线程池，所以用不着预先初始化好休眠的线程  
	        int actualThreadCount = minTaskCount > 0 ? threadCount  
	                : remainTaskCount;  
	        // 要启动的线程数组，以及每个线程要执行的任务列表  
	        List[] taskListPerThread = new List[actualThreadCount];  
	        int taskIndex = 0;  
	        // 平均分配后多余任务，每附加给一个线程后的剩余数，重新声明与 remainTaskCount  
	        // 相同的变量，不然会在执行中改变 remainTaskCount 原有值，产生麻烦  
	        int remainIndces = remainTaskCount;  
	        for (int i = 0; i < taskListPerThread.length; i++) {  
	            taskListPerThread[i] = new ArrayList();  
	            // 如果大于零，线程要分配到基本的任务  
	            if (minTaskCount > 0) {  
	                for (int j = taskIndex; j < minTaskCount + taskIndex; j++) {  
	                    taskListPerThread[i].add(taskList.get(j));  
	                }  
	                taskIndex += minTaskCount;  
	            }  
	            // 假如还有剩下的，则补一个到这个线程中  
	            if (remainIndces > 0) {  
	                taskListPerThread[i].add(taskList.get(taskIndex++));  
	                remainIndces--;  
	            }  
	        }  
	        // 打印任务的分配情况  
	        System.out.println("总任务数： " + taskList.size());
	        for (int i = 0; i < taskListPerThread.length; i++) {  
	            System.out.println("线程 "  
	                    + i  
	                    + " 的任务数："  
	                    + taskListPerThread[i].size() );
//	                    + " 区间["  
//	                    +  taskListPerThread[i].size()*i
//	                    + ","  
//	                    +  taskListPerThread[i].size()*(i + 1)
//	                    + "]");  
	        }  
	        return taskListPerThread;  
	    }  
	  /**
	   * 文件是否存在  true 存在
	   * @param path
	   * @return
	   */
	  public static boolean isExistTheFile(String path) {
		  File file = new File(path);
		  if (file.exists()) {
			return true;
		  }
		  return false;
	  }
	  
    
}
