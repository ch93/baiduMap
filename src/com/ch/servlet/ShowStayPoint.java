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

public class ShowStayPoint extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
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
		
		String path = MyConstant.filePath + "stayPointData/" + tempFileName +"/stayPoint.json";
		String path1 = MyConstant.filePath + "stayPointData/" + tempFileName +"/clusteredStayPoint.json";
		File file = new File(path1);
		String result = "";
		if (file.exists()) {
			result = MTools.readFile(path1);
		} else {
			result = MTools.readFile(path);
		}
		if ("" == result) {
			request.setAttribute("error", "there is no data in stayPoint collection");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} else {
		    request.setAttribute("stayPoint", result);
		    request.getRequestDispatcher("showStayPoint.jsp").forward(request, response);
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
