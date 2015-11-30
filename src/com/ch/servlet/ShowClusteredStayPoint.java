package com.ch.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ch.util.MTools;
import com.ch.util.MyConstant;

/**
 * @author: ch
 * @date:2015-11-29 上午10:08:43
 * 
 */
public class ShowClusteredStayPoint extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int userid = Integer.valueOf(request.getParameter("userid"));
		String tempFileName = "";
		if (userid < 10) {
			tempFileName = "00" + userid;
		} else if (userid < 100) {
			tempFileName = "0" + userid;
		} else {
			tempFileName = "" + userid;
		}
		
		String path1 = MyConstant.filePath + "stayPointData/" + tempFileName +"/clusteredStayPoint.json";
		String result = MTools.readFile(path1);
	    
		
		if ("" == result) {
			request.setAttribute("error", "there is no data in clustered stayPoint collection");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} else {
		    request.setAttribute("stayPoint", result);
		    request.getRequestDispatcher("showClusteredStayPoint.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
