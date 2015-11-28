package com.ch.test;

import java.text.DecimalFormat;

/**
 * 进度统计
 * @author ch
 *
 */
public class TimeThread extends Thread {
	
	int i = 0;
	
	int totalNum = 1;
	
	Boolean flag = false;
	
	double progress = 0.0;
	 
    public void run() {
    	while (!flag) {
    		progress =  ((double)i/totalNum)*100;
    		
    		System.out.println("进度 ： " +  Math.round(progress)  +"%  "+ i + "  "+ totalNum);
    		try {
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	}


	public int getI() {
		return i;
	}


	public void setI(int i) {
		this.i = i;
	}


	public int getTotalNum() {
		return totalNum;
	}


	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}


	public Boolean getFlag() {
		return flag;
	}


	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
    
    
    
	}
