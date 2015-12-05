package com.ch.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 多线程任务的基类
 *   能智能提醒当前任务经度
 * @author ch
 *
 */
public class MutiThread extends Thread{

	private List<Object> filelist;
	//当前任务数
	private int currentN = 0;
	//总任务数
	private int totalN = 1;
	//定时器
	private Timer timer;
	//当前为第几个线程
	private int CurrentT = -1;
	
	public MutiThread(List<Object> filelist, int CurrentT) {
		this.filelist = filelist;
		timer = new Timer();
		this.CurrentT = CurrentT;
	}
	
	public int getCurrentN() {
		return currentN;
	}
	
	public void setCurrentN(int CurrentT) {
		this.currentN = CurrentT;
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
//			ExtractionStayPoint stayPoint = new ExtractionStayPoint();
//			for (int i = 0; i < filelist.size(); i++) {
//				FileAtrribute fileAtrribute = filelist.get(i);
//				stayPoint.collectStayPoint(fileAtrribute.getFilePath(), fileAtrribute.getFileNum());
//	//			json.JsonToLocal(fileAtrribute.getFilePath(), fileAtrribute.getFileNum());
//				currentN = i;
//			}
			doProcess();
		} else {
			System.out.println("this is no data in filelist");
		}
		System.out.println("***************** 线程 情况 **************");	
		System.out.println("***    线程 "+CurrentT+" 任务完成！      ");
		System.out.println("*****************  end  **************");	
	    timer.cancel();
	}
	
	public void doProcess() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 时间提醒
	 */
	protected void TimeTip() {
		final long startTime=System.currentTimeMillis();   //获取开始时间

        timer.schedule(new TimerTask() {  
            public void run() {  
            	long endTime=System.currentTimeMillis(); //获取结束时间 
            	float tempm = (float)currentN/totalN;
        		System.out.println("\n*****************   线程 情况          **************");	
                System.out.println("**  线程"+ CurrentT + "  当前进度： " + (int)(tempm*100) + "%" + "  程序运行： "+(endTime-startTime)/(1000*60)+"min");
                System.out.println("*****************     end    **************\n");
//                System.out.println("线程"+ CurrentT + " deal with " + filelist.get(currentN).getFilePath());
            }
        }, 1000, 1000*30);// 设定指定的时间time,此处为0.5秒  
	}
}
