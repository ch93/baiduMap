package com.ch.loaddata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ch.bean.FileAtrribute;
import com.ch.bean.GPSpoint;
import com.ch.test.TestLog;
import com.ch.util.MyConstant;
/**
 * 读取GPS文件
 * @author ch
 *
 */
public class LoadDataFromLog {
	//文件集合
	private ArrayList<FileAtrribute> filelist = new ArrayList<FileAtrribute>();
	
	private int userid = 0;
	
	public void readFolder(String path) {

	}
	
	public void fileCount() {
		System.out.println("file num: " + filelist.size());
	}
	
	
	
	/**
	 * 计算各个文件的个数
	 * @param filelist1
	 * @param fileNum 文件名
	 * @return
	 */
	public int countFile(int fileNum) {
		int count = 0;
		for (FileAtrribute fileAtrribute : filelist) {
			int id = fileAtrribute.getFileNum();
			if (id == fileNum ) {
				count++;
			}
		}
		
		return count;
	}
	
	 /*
	  * 通过递归得到某一路径下所有的目录及其文件
	  */
	public void getFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			String name = file.getName();
			int userTemple = 0;
			if (isNumeric(name)) {
				System.out.println("文件名 " +  name);
				userTemple =  Integer.valueOf(name);
			}
			if (userid < userTemple) {
				userid = userTemple;
			}
			//去除lable文件
			if ("labels".equals(name)) {
				continue;
			}
			if (file.isDirectory()) {
				/*
				 * 递归调用
				 */
				getFiles(file.getAbsolutePath());
//				filelist.add(file.getAbsolutePath());
//				System.out.println("显示 " + filePath + "下所有子目录及其文件 "
//						+ file.getAbsolutePath());
			} else {
				String filepath = file.getAbsolutePath();
//				System.out.println("显示 " + filePath + " 下所有子目录 " + filepath);
				FileAtrribute fileAtrribute = new FileAtrribute(userid, filepath);
				filelist.add(fileAtrribute);
			}
		}
		
	}
	
	 /*
	  * 通过递归得到某一路径下所有的目录及其文件
	  */
	public void getFiles(int startNum, int endNum) {
		String tempFileName = "";
		for (int i = startNum; i < endNum; i++) {  //一共有178个文件夹
			if (i < 10) {
				tempFileName = "00" + i;
			} else if (i < 100) {
				tempFileName = "0" + i;
			} else {
				tempFileName = "" + i;
			}
			String filePath = MyConstant.filePath + "Geolife Trajectories 1.2/Data/" + tempFileName +"/Trajectory";
			File root = new File(filePath);
			File[] files = root.listFiles();
			for (File file : files) {
				String filepath = file.getAbsolutePath();
//				System.out.println("显示 " + filePath + " 下所有子目录 " + filepath);
				FileAtrribute fileAtrribute = new FileAtrribute(i, filepath);
				filelist.add(fileAtrribute);
			}
		}
	}
	
	/**
	 * 读取文本文件
	 * @param name
	 * @return
	 */
	public List<GPSpoint> readTextFile(String filePath, int user) {
		List<GPSpoint> gPSpoints = new ArrayList<GPSpoint>();
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
						gPSpoints.add(point);
					}
				}
				line++;
			}  
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
		return gPSpoints;
	}
	/**
	 * 读取文本
	 * @param filePath 路径
	 * @return 本文内容
	 */
//	public String readFile(String filePath) {
//		String result = "";
//		File file = new File(filePath);
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//			String tempString = null;
//			while ((tempString = reader.readLine()) != null) {
//				result += tempString;
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return result;
//		
//	}
	
	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]{3}");
	    return pattern.matcher(str).matches();   
	}

	public ArrayList<FileAtrribute> getFilelist() {
		return filelist;
	}

	public void setFilelist(ArrayList<FileAtrribute> filelist) {
		this.filelist = filelist;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

}
