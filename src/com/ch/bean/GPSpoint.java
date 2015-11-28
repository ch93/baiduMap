package com.ch.bean;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * GPS点信息
 * @author ch
 *
 */
public class GPSpoint {
	/**
	 * 点的用户
	 */
	private int userid;
	/**
	 * 纬度
	 */
	private double lat;
	/**
	 * 经度
	 */
	private double lngt;
	/**
	 * 高度
	 */
	private float height;
	/**
	 * 时间
	 */
	private Timestamp gtime;
	/**
	 * 数值格式的时间
	 */
	private double itime;
	
	public GPSpoint(int userid, double lat, double lngt, float height,
			Timestamp gtime) {
		this.userid = userid;
		this.lat = lat;
		this.lngt = lngt;
		this.height = height;
		this.gtime = gtime;
	}
	
	public GPSpoint(int userid, double lat, double lngt, float height,
			Timestamp gtime, double itime) {
		this.userid = userid;
		this.lat = lat;
		this.lngt = lngt;
		this.height = height;
		this.gtime = gtime;
		this.itime = itime;
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
	
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public Timestamp getGtime() {
		return gtime;
	}
	public void setGtime(Timestamp gtime) {
		this.gtime = gtime;
	}
	
	public double getItime() {
		return itime;
	}

	public void setItime(double itime) {
		this.itime = itime;
	}

	/**
	 * 输出经纬度信息
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("lat", lat);
			jObject.put("lngt", lngt);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jObject;
		
	}
	/**
	 * 输出全部信息
	 * @return
	 */
	public JSONObject toJsonAll() {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("userid", userid);
			jObject.put("lat", lat);
			jObject.put("lngt", lngt);
			jObject.put("height", height);
			jObject.put("gtime", gtime);
			jObject.put("itime", itime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jObject;
		
	}
	
}
