package com.ch.dbscan;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import com.ch.bean.StayPoint;

public class Point extends StayPoint {
	private double x;
	private double y;
	//簇号
	private int clusterID;
	private boolean isKey;
	private boolean isClassed;

	public Point(int userid, double lat, double lngt, Timestamp arvT,
			Timestamp levT, double iArvT, double iLevT) {
//		super(lat, lngt, arvT, levT, iArvT, iLevT);
		super(userid, lat, lngt, arvT, levT, iArvT, iLevT);
		x = lat;
		y = lngt;
	}
	
	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
		this.isClassed = true;
	}

	public boolean isClassed() {
		return isClassed;
	}

	public void setClassed(boolean isClassed) {
		this.isClassed = isClassed;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

//	public Point(String str) {
//		String[] p = str.split(",");
//		this.x = Integer.parseInt(p[0]);
//		this.y = Integer.parseInt(p[1]);
//	}

	public String print() {
		return  + this.getLat() + " , " + this.getLngt() + " , " + this.getArvT() + " , " + this.getLevT();
	}
	
	public JSONObject toJson() {
		JSONObject jObject = new JSONObject();
		try {
//			jObject.put("userid", this.);
			jObject.put("lat", this.getLat());
			jObject.put("lngt", this.getLngt());
			jObject.put("arvT", this.getArvT());
			jObject.put("levT", this.getLevT());
			jObject.put("iArvT", this.getiArvT());
			jObject.put("iLevT", this.getiLevT());
			jObject.put("clusterID", clusterID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObject;
	}
}
