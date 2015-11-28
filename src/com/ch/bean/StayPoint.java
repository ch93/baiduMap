package com.ch.bean;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 停留点信息
 * @author ch
 *
 */
public class StayPoint {
	/**
	 * 点的用户
	 */
	private int userid;
	/**
	 * 经度
	 */
	private double lngt;
	/**
	 * 纬度
	 */
	private double lat;
	/**
	 * 到达时间
	 */
	private Timestamp arvT;
	/**
	 * 离开时间
	 */
	private Timestamp levT;
	
	/**
	 * 到达时间
	 */
	private double iArvT;
	/**
	 * 离开时间
	 */
	private double iLevT;
	
	public StayPoint(double lat, double lngt, Timestamp arvT, Timestamp levT) {
		this.lat = lat;
		this.lngt = lngt;
		this.arvT = arvT;
		this.levT = levT;
	}
	
	public StayPoint(int userid, double lat, double lngt, Timestamp arvT,
			Timestamp levT) {
		this.userid = userid;
		this.lat = lat;
		this.lngt = lngt;
		this.arvT = arvT;
		this.levT = levT;
	}
	
	public StayPoint(double lat, double lngt, Timestamp arvT,
			Timestamp levT, double iArvT, double iLevT) {
//		this.userid = userid;
		this.lat = lat;
		this.lngt = lngt;
		this.arvT = arvT;
		this.levT = levT;
		this.iArvT = iArvT;
		this.iLevT = iLevT;
	}
	
	public StayPoint(int userid, double lat, double lngt, Timestamp arvT,
			Timestamp levT, double iArvT, double iLevT) {
		this.userid = userid;
		this.lat = lat;
		this.lngt = lngt;
		this.arvT = arvT;
		this.levT = levT;
		this.iArvT = iArvT;
		this.iLevT = iLevT;
	}

	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	/**
	 * 纬度
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * 纬度
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * 经度
	 */
	public double getLngt() {
		return lngt;
	}
	/**
	 * 经度
	 */
	public void setLngt(double lngt) {
		this.lngt = lngt;
	}
	public Timestamp getArvT() {
		return arvT;
	}
	public void setArvT(Timestamp arvT) {
		this.arvT = arvT;
	}
	public Timestamp getLevT() {
		return levT;
	}
	public void setLevT(Timestamp levT) {
		this.levT = levT;
	}
	
	
	public double getiArvT() {
		return iArvT;
	}

	public void setiArvT(double iArvT) {
		this.iArvT = iArvT;
	}

	public double getiLevT() {
		return iLevT;
	}

	public void setiLevT(double iLevT) {
		this.iLevT = iLevT;
	}

	/**
	 * 返回json对象
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jObject = new JSONObject();
		try {
//			jObject.put("userid", userid);
			jObject.put("lat", lat);
			jObject.put("lngt", lngt);
			jObject.put("arvT", arvT);
			jObject.put("levT", levT);
			jObject.put("iArvT", iArvT);
			jObject.put("iLevT", iLevT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObject;
	}
	
}
