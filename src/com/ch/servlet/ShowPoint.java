package com.ch.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.ch.bean.GPSpoint;
import com.ch.loaddata.LoadDataFromLog;
import com.ch.util.CoordinateConvert;
import com.ch.util.MTools;
import com.ch.util.MyConstant;

public class ShowPoint extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int userid = Integer.valueOf(request.getParameter("userid"));
		String tempFileName = "";
		if (userid < 10) {
			tempFileName = "00" + userid;
		} else if (userid < 100) {
			tempFileName = "0" + userid;
		} else if (userid < 178){
			tempFileName = "" + userid;
		} else {
			tempFileName = "177";
		}
		String fileName = request.getParameter("fileName");
		LoadDataFromLog load = new LoadDataFromLog();
//		20081026134407
//		String filePath = MyConstant.filePath + "Geolife Trajectories 1.2/Data/"+tempFileName+"/Trajectory/"+fileName+".plt";
		String filePath = MyConstant.filePath + "Data/"+tempFileName+"/Trajectory/"+fileName+".plt";
		List<GPSpoint> GPSpoints = load.readTextFile(filePath, 0);
		JSONArray jArray = new JSONArray();
		for (GPSpoint GPSpoint : GPSpoints) {
			
			double wgLat = GPSpoint.getLat();
			double wgLon = GPSpoint.getLngt();
			//wgs84  转 百度坐标系
			double[] temp = CoordinateConvert.wgs2BD09(wgLat, wgLon);
			GPSpoint.setLat(temp[0]);
			GPSpoint.setLngt(temp[1]);
			
			jArray.put(GPSpoint.toJsonAll());
		}
		request.setAttribute("gpsPoint", jArray.toString());
		request.getRequestDispatcher("showPoint.jsp").forward(request, response);

		
		
//		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}


}
