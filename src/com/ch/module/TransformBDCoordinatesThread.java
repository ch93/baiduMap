package com.ch.module;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.ch.bean.FileAtrribute;
import com.ch.loaddata.DataFromLogToJson;

public class TransformBDCoordinatesThread extends Thread {

private ArrayList<FileAtrribute> filelist;
	
	private int currentN = 0;
	
	private int totalN = 1;
	
	private Timer timer;
	//当前为第几个线程
	private int CurrentT = -1;
	
	public TransformBDCoordinatesThread(ArrayList<FileAtrribute> filelist, int CurrentT) {
		this.filelist = filelist;
		timer = new Timer();
		this.CurrentT = CurrentT;
	}
	
	public int getCurrentN() {
		return currentN;
	}

	public int getTotalN() {
		return totalN;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//智能提醒
		    TimeTip();
		    totalN = filelist.size();
			if (totalN > 0) {
			DataFromLogToJson json = new DataFromLogToJson();
			for (int i = 0; i < filelist.size(); i++) {
				FileAtrribute fileAtrribute = filelist.get(i);
				json.JsonToLocal(fileAtrribute.getFilePath(), fileAtrribute.getFileNum());
				currentN = i;
			}
		} else {
			System.out.println("this is no data in filelist /ExtractionStayPoint");
		}
		System.out.println("线程 "+CurrentT+" 任务完成！");
		//关闭定时器
	    timer.cancel();
	}
	
	/**
	 * 定时提示进度
	 */
	private void TimeTip() {
		final long startTime=System.currentTimeMillis();   //获取开始时间

        timer.schedule(new TimerTask() {  
            public void run() {  
            	long endTime=System.currentTimeMillis(); //获取结束时间 
            	float tempm = (float)currentN/totalN;
                System.out.println("线程"+ CurrentT + "  当前进度： " + (int)(tempm*100) + "%" + "  程序运行： "+(endTime-startTime)/(1000*60)+"min");  
            }
        }, 1000, 1000*30);// 设定指定的时间time,此处为0.5秒  
	}
	
}
