package com.ch.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ch.bean.GPSpoint;
import com.ch.net.HttpNet;
import com.ch.util.MyConstant;

/**
 * @author: ch
 * @date:2015-12-1 下午10:09:16
 * 
 */
public class Geocoding {
     
	/**
	 * 获取
	 * @param lat
	 * @param lngt
	 */
	public void getAddress(double lat, double lngt) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ak", MyConstant.MyBaiduKey));
		params.add(new BasicNameValuePair("location", ""+lat+","+lngt));
		params.add(new BasicNameValuePair("output", "json"));
		params.add(new BasicNameValuePair("pois", "1"));
		HttpNet httpNet = new HttpNet();
		String url = "http://api.map.baidu.com/geocoder/v2/";
		String result = httpNet.doPost(url, params);
		try {
			JSONObject jObject = new JSONObject(result);
			//百度转换后的状态码
			int status = jObject.getInt("status");
			if (status == 0) { //坐标转换成功
				JSONArray jArray = jObject.getJSONObject("result").getJSONArray("pois");
				if (jArray.length() > 0) {
					JSONObject jObject1 = jArray.getJSONObject(0);
					String poiType = jObject1.getString("poiType");
					String tag = jObject1.getString("tag");
					System.out.println(tag);
				}
//				for (int k = 0; k < jArray.length(); k++) {
//					JSONObject jObject1 = jArray.getJSONObject(k);
//					double lngt = jObject1.getDouble("x");
//					double lat = jObject1.getDouble("y");
//					GPSpoint point = GPSpoints.get(i*100 + k);
//					point.setLat(lat);
//					point.setLngt(lngt);
//				}
			} else {
				System.out.println("百度转换后的状态码： " + result + " "+httpNet.getURLPath());
//				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//从百度api调用失败的GPS文件记录下来，下次自己在调用
//			log.error(filePath);
			e.printStackTrace();
			
		}
		
	}
	
	public static void main(String[] args) {
		Geocoding geocoding = new Geocoding();
		geocoding.getAddress(40.00026940425533, 116.32729625531914);
		
	}
}
