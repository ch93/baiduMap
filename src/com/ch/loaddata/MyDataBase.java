package com.ch.loaddata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import org.apache.log4j.Logger;

import com.ch.bean.GPSpoint;

import sun.awt.SunHints.Value;



public class MyDataBase {
	
//	private final static String JDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";//SQL��ݿ�����
//	private final static String user="sa";//���Լ��������û����ֺ�����!
//	private final static String password="071388";
//	private final static String dataBaseName = "RestaurantDB";

//	private Connection connection ; 
	Logger log = Logger.getLogger(MyDataBase.class);
	
	
	String[] tableNames = new String[]{
			"user2007",
			"user2008",
			"user2009",
			"user2010",
			"user2011",

	};
	
	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = ConnectionPool.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	public Boolean insertGPSdata(List<GPSpoint> GPSpoints){
//		CALL inserDataToUser07(1,33.45,116.6,345,'1970-01-01 00:00:00.0');
		String sql = "";
		Boolean rsesult = false;
		if(GPSpoints.size() > 0) {
			 int year = GPSpoints.get(0).getGtime().getYear()+1900;
			 if (year == 2007) {
				 sql = "INSERT into user2007 VALUES(?,?,?,?,?)";
			} else if(year == 2008) {
				sql = "INSERT into user2008 VALUES(?,?,?,?,?)";
			} else if(year == 2009) {
				sql = "INSERT into user2009 VALUES(?,?,?,?,?)";
			} else if(year == 2010) {
				sql = "INSERT into user2010 VALUES(?,?,?,?,?)";
			} else {
				sql = "INSERT into user2011 VALUES(?,?,?,?,?)";
			}
		}
		try {
			Connection connection = ConnectionPool.getConnection();
			connection.setAutoCommit(false);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			for (GPSpoint gpSpoint : GPSpoints) {
				pstmt.setInt(1, gpSpoint.getUserid());
				pstmt.setDouble(2, gpSpoint.getLat());
				pstmt.setDouble(3, gpSpoint.getLngt());
				pstmt.setFloat(4, gpSpoint.getHeight());
				pstmt.setTimestamp(5, gpSpoint.getGtime());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			connection.commit();
			ConnectionPool.release(connection, pstmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("SQLException ��ʾ " + e.getMessage());
		}
		return rsesult;
	}

	/**
	 * ����ÿ����ļ�¼��
	 * @param tableName 
	 * @return
	 */
	public int countGPSInfo(String tableName) {
		String sql = "select COUNT(*) FROM " + tableName;
		int rsesult = 0;
		try {
			Connection connection = ConnectionPool.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()){
				rsesult = rs.getInt(1);
			} else {
				System.out.println("there is no data in " + tableName);
			}
			ConnectionPool.release(connection, stmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsesult;
	}
	
	/**
	 * ����GPS��Ϣ���б�ļ�¼��
	 * @return
	 */
	public int countcountGPSInfoTatal() {
		int rsesult = 0;
		for (String tableName : tableNames) {
			rsesult += countGPSInfo(tableName);
		}
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			pstmt.execute(sql);
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()){
//				rsesult = rs.getInt(1);
//			} else {
//				System.out.println("there is no data in myfilm" );
//			}
//			ConnectionPool.release(connection, pstmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return rsesult;
		
	}

//	/**
//	 * ������Ӱ 
//	 * @param key ����������
//	 * @param val ��������ֵ
//	 * @return
//	 */
//	public List<FilmInfo> SerchOneFilm(String key, String val) {
//		List<FilmInfo> filmInfos = new ArrayList<FilmInfo>();
//		String sql = "";
////		String[] tableNames = new String[]{
////				"film_action",
////				"film_adventure",
////				"film_cartoon",
////				"film_comedy",
////				"film_crime",
////				"film_drama",
////				"film_family",
////				"film_fantasy",
////				"film_horror",
////				"film_mystery",
////				"film_other",
////				"film_romance",
////				"film_thriller",
////				"film_war"
////		};
//		
//		String[] sqls = new String[tableNames.length];
//		String whereStr = "";
////		String whereStr = "where filmYear = 2014";
//		if ("filmYear".equals(key)) {
//			//BUG �������ĸ�ʽ ���
//			if (!val.matches("\\d{4}")) {
//				return null;
//			}
//			whereStr = "where "+ key +" = "+ val;
//		} else {
//			//where filmName LIKE '%��%';
//			// key ����Ϊ
////			filmName
////			filmName1
////			lable
////			filmArea
////			director
////			screenwriter
////			filmRole
//			whereStr = "where "+ key +" LIKE '%"+ val+"%'";
//		}
//
//
//		for (int i = 0; i < sqls.length; i++) {
//			sqls[i] = " (SELECT * FROM " + tableNames[i] + " "+whereStr + ") ";
//		}
//		// UNION   UNION ALL
//		for (int i = 0; i < sqls.length; i=i+2) {
//			sql = sql + sqls[i] + "UNION" +sqls[i+1]+ "UNION";
//		}
//		sql = sql.substring(0, sql.length()-5);
//		sql = sql + " ORDER BY filmYear DESC";
//		System.out.println(this.toString() +" "+sql);
//
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(sql);
//			while(rs.next()) {
//				FilmInfo filmInfo = rsToFilmInfo(rs);
//				filmInfos.add(filmInfo);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return filmInfos;
//		
//	}
	
	/**
	 * ������Ӱ ,������������ҳ
	 * @param key ����������
	 * @param val ��������ֵ
	 * @param pageNum ҳ�� eg  1��2��3
	 * @param vol ҳ������ eg 20����¼
	 * @return
	 */
//	public List<FilmInfo> SerchOneFilm(String key, String val, int pageNum, int vol) {
//		List<FilmInfo> filmInfos = new ArrayList<FilmInfo>();
//		String sql = "";
//		String[] sqls = new String[tableNames.length];
//		String whereStr = "";
//		if ("filmYear".equals(key)) {
//			//BUG �������ĸ�ʽ ���
//			if (!val.matches("\\d{4}")) {
//				return null;
//			}
//			whereStr = "where "+ key +" = "+ val;
//		} else {
//			//where filmName LIKE '%��%';
//			// key ����Ϊ
////			filmName
////			filmName1
////			lable
////			filmArea
////			director
////			screenwriter
////			filmRole
//			whereStr = "where "+ key +" LIKE '%"+ val+"%'";
//		}
////		String whereStr = "where filmYear = 2014";
//
//		for (int i = 0; i < sqls.length; i++) {
//			sqls[i] = " (SELECT * FROM " + tableNames[i] + " "+whereStr + ") ";
//		}
//		// UNION   UNION ALL
//		for (int i = 0; i < sqls.length; i=i+2) {
//			sql = sql + sqls[i] + "UNION" +sqls[i+1]+ "UNION";
//		}
//		sql = sql.substring(0, sql.length()-5);
//		
//		int off = 0;
//		if (pageNum > 1) {
//			off = pageNum*vol - 1;
//		}
//		sql = sql + " ORDER BY filmYear DESC LIMIT "+ off +", " + vol;
//		System.out.println(this.toString() + " " +sql);
//
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(sql);
//			while(rs.next()) {
//				FilmInfo filmInfo = rsToFilmInfo(rs);
//				filmInfos.add(filmInfo);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return filmInfos;
//		
//	}
	
	/**
	 * ��ݿ��ҳ
	 * @param tableName ����
	 * @param pageNum ҳ�� eg  1��2��3
	 * @param vol ҳ������ eg 20����¼
	 * @return
	 */
//	public List<FilmInfo> limitFilm(String tableName, int pageNum, int vol) {
//		List<FilmInfo> filmInfos = new ArrayList<FilmInfo>();
//		int off = 0;
//		if (pageNum > 1) {
//			off = pageNum*vol - 1;
//		}
//		String sql = "SELECT * FROM "+ tableName + " ORDER BY filmYear DESC LIMIT "+ off +", " + vol;
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(sql);
//			while (rs.next()) {
////				String filmName = rs.getString("filmName");
////				String filmName1 = rs.getString("filmName1");
////				String lable = rs.getString("lable");
////				String filmArea = rs.getString("filmArea");
////				int filmYear = rs.getInt("filmYear");
////				String director = rs.getString("director");
////				String screenwriter = rs.getString("screenwriter");
////				String filmRole = rs.getString("filmRole");
////				String filmImdb = rs.getString("filmImdb");
////				float filmRank = rs.getFloat("filmRank");
////				String filmTorrent = rs.getString("filmTorrent");
////				String filmPic = rs.getString("filmPic");
//				FilmInfo filmInfo = rsToFilmInfo(rs);
//				filmInfos.add(filmInfo);
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return filmInfos;
//		
//	}
//	
//	/**
//	 * ��ResultSet��ֵȡ�����FilmInfo
//	 * @param rs
//	 * @return
//	 */
//	private FilmInfo rsToFilmInfo(ResultSet rs) {
//		String filmName;
//		try {
//			filmName = rs.getString("filmName");
//			String filmName1 = rs.getString("filmName1");
//			String lable = rs.getString("lable");
//			String filmArea = rs.getString("filmArea");
//			int filmYear = rs.getInt("filmYear");
//			String director = rs.getString("director");
//			String screenwriter = rs.getString("screenwriter");
//			String filmRole = rs.getString("filmRole");
//			String filmImdb = rs.getString("filmImdb");
//			float filmRank = rs.getFloat("filmRank");
//			String filmTorrent = rs.getString("filmTorrent");
//			String filmPic = rs.getString("filmPic");
//			FilmInfo filmInfo = new FilmInfo(filmName, filmName1, lable, filmArea, 
//											filmYear, director, screenwriter, filmRole, 
//											filmImdb, filmRank, filmTorrent, filmPic);
//			return filmInfo;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//	/**
//	 * ���tableMenu
//	 * @return
//	 */
//	public static List<TableMenu> readTableMenu() {
//		List<TableMenu> listOrder = new ArrayList<TableMenu>();
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			Statement stmt = connection.createStatement();
//			ResultSet rs=stmt.executeQuery("SELECT * FROM  tableMenu");//����SQL����ѯ���(����)
//			while(rs.next()) {
//				//���ÿ���ֶ�
//				System.out.println(rs.getInt("dishID")+"\t"+rs.getString("dishName")+"\t"+rs.getInt("dishPrice") + "\t"+rs.getString("dishType"));
//				int dishID = rs.getInt("dishID");
//				String dishName = rs.getString("dishName").trim();
//				int dishPrice = rs.getInt("dishPrice");
//				String dishType = rs.getString("dishType").trim();
//				
//				TableMenu tableMenu = new TableMenu(dishID, dishName, dishPrice, dishType);
//				listOrder.add(tableMenu);
//			}
////			System.out.println(rs.getRowId("grade"));
//			System.out.println("��ȡ���");
//			//�ر�����
//			ConnectionPool.release(connection, stmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return listOrder;
//	}
//	
//	/**
//	 * ���tableMenu
//	 * @return
//	 */
//	public static List<TableMenu> inqueryTableMenu(String where) {
//		List<TableMenu> listOrder = new ArrayList<TableMenu>();
//		String sql = "SELECT * FROM  tableMenu  where " + where;
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			Statement stmt = connection.createStatement();
//			ResultSet rs=stmt.executeQuery(sql);//����SQL����ѯ���(����)
//			while(rs.next()) {
//			//���ÿ���ֶ�
//				System.out.println(rs.getInt("dishID")+"\t"+rs.getString("dishName")+"\t"+rs.getInt("dishPrice") + "\t"+rs.getString("dishType"));
//				int dishID = rs.getInt("dishID");
//				String dishName = rs.getString("dishName").trim();
//				int dishPrice = rs.getInt("dishPrice");
//				String dishType = rs.getString("dishType").trim();
//				
//				TableMenu tableMenu = new TableMenu(dishID, dishName, dishPrice, dishType);
//				listOrder.add(tableMenu);
//			}
////			System.out.println(rs.getRowId("grade"));
//			System.out.println("��ȡ���");
//			//�ر�����
//			ConnectionPool.release(connection, stmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return listOrder;
//	}
//	/**
//	 * ����в���һ�����TableMenu
//	 * @param dishName
//	 * @param dishPrice
//	 * @param dishType
//	 */
//	public static void insertTableMenu(String dishName, int dishPrice, String dishType) {
//		String sql = "insert into tableMenu values (?, ?, ?)";
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			pstmt.setString(1, dishName);
//			pstmt.setInt(2, dishPrice);
//			pstmt.setString(3, dishType);
//			pstmt.executeUpdate();
//			ConnectionPool.release(connection, pstmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	/**
//	 * ����в���������TableMenu
//	 */
//	public static void insertTableMenu(List<TableMenu> mTableMenus) {
//		String sql = "insert into tableMenu values (?, ?, ?)";
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			for (int i = 0; i < mTableMenus.size(); i++) {
//				TableMenu tempTableMuan = mTableMenus.get(i);
//				String dishName = tempTableMuan.getDishName();
//				int dishPrice = tempTableMuan.getDishPrice();
//				String dishType = tempTableMuan.getDishType();
//				pstmt.setString(1, dishName);
//				pstmt.setInt(2, dishPrice);
//				pstmt.setString(3, dishType);
//				pstmt.executeUpdate();
//				pstmt.clearParameters();
//			}
//			ConnectionPool.release(connection, pstmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

//	/**
//	 * �����û�
//	 * @param tableNum
//	 * @param userNum
//	 * @param remark
//	 * @param orderNum
//	 */
//	public static void insertUser(int tableNum, int userNum, String remark, List<Integer> orderNum) {
//		boolean flag = true;
//		int userID = 0;
//		String sql = "insert into tableUser values(?, GETDATE(), ?, 0, ?)";
//		Connection connection = null;
//		try {
//			connection = ConnectionPool.getConnection();
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			pstmt.setInt(1, userNum);
//			pstmt.setInt(2, tableNum);
//			pstmt.setString(3, remark);
//			if (pstmt.executeUpdate() < 1) {
//				flag = false;
//			} 
//			pstmt.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 sql = "select UserID from tableUser";
//		try {
//			PreparedStatement pstmt = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			ResultSet rs = pstmt.executeQuery();
//			rs.afterLast();
//			if (rs.previous()){
//				 userID = rs.getInt(1);
//				 System.out.println("userID : " + userID);
//			} else {
//				System.out.println("there is no data");
//			}
//			pstmt.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		 sql = "insert into tableFood values (? , 1, ?)";
//			try {
//				PreparedStatement pstmt = connection.prepareStatement(sql);
//				for (int i = 0; i < orderNum.size(); i++) {
//					pstmt.setInt(1, userID);
//					pstmt.setInt(2, orderNum.get(i));
//					if (pstmt.executeUpdate() < 1) {
//						flag = false;
//					} 
//					pstmt.clearParameters();
//				}
//				ConnectionPool.release(connection, pstmt);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
////			sql = "update tableResources set isOccupy = ?, UserAmount = UserAmount + ? where deskID = ?";
////			try {
////				PreparedStatement pstmt = connection.prepareStatement(sql);
////				pstmt.setByte(1, (byte) 1);
////				pstmt.setInt(2, userNum);
////				pstmt.setInt(3, tableNum);
////				if (pstmt.executeUpdate() < 1) {
////					flag = false;
////				}
////				pstmt.close();
////			} catch (SQLException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			try {
////				connection.close();
////			} catch (SQLException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
//	}
//	
//	/**
//	 * ��ȡһ�����ӵ� �����û���Ϣ
//	 * @param deskID
//	 * @return
//	 */
//	public static List<OrderList> getOrderList(int deskID) {
//		List<OrderList> mOrderLists = new ArrayList<OrderList>();
//		String sql = "select * from tableUser where CONVERT(varchar(30), businessTime, 112) = CONVERT(varchar(30), GETDATE(), 112) and isPayed = 0 and deskID = ?";
//		Connection connection = null;
//		try {
//			connection = ConnectionPool.getConnection();
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			pstmt.setInt(1, deskID);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()){
//				int UserID = rs.getInt("UserID");
//				int UserAmount = rs.getInt("UserAmount");
//				OrderList mOrderList = new OrderList(UserID, UserAmount);
//				mOrderLists.add(mOrderList);
//			}
//			pstmt.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		sql = "select * from tableMenu where dishID in(select dishID from tableFood where UserID = ?)";
//
//		try {
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			for (int i = 0; i < mOrderLists.size(); i++) {
//				int UserID = mOrderLists.get(i).getUserID();
//				pstmt.setInt(1, UserID);
//				ResultSet rs = pstmt.executeQuery();
//				while (rs.next()) {
//					int dishID = rs.getInt("dishID");
//					String dishName = rs.getString("dishName");
//					int dishPrice = rs.getInt("dishPrice");
//					String dishType = rs.getString("dishType");
//					TableMenu mTableMenu = new TableMenu(dishID, dishName, dishPrice, dishType);
//					mOrderLists.get(i).addDises(mTableMenu);
//				}
//				pstmt.clearParameters();
//			}
//			ConnectionPool.release(connection, pstmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return mOrderLists;
//	}
//	
//	/**
//	 * ����
//	 * @param UserID
//	 * @param deskID
//	 * @param userAmount
//	 */
//	public static void paymentByUerID(int userID, int deskID, int userAmount) {
//		String sql = "execute paymemt ? ,? ,?";
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			pstmt.setInt(1, userID);
//			pstmt.setInt(2, deskID);
//			pstmt.setInt(3, userAmount);
//			pstmt.executeUpdate();
//			ConnectionPool.release(connection, pstmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	/**
//	 * ��ò˵Ĳ���ʹ�����
//	 * @return
//	 */
//	public static List<StaticDish> getStaticDish() {
//		List<StaticDish> mStaticDishs = new ArrayList<StaticDish>();
//		String sql = "execute StatisticsDish";
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(sql);
//			while (rs.next()) {
//				String dishName = rs.getString("dishName"); 
//				int dishNum = rs.getInt("num");
//				StaticDish mStaticDish = new StaticDish(dishName, dishNum);
//				mStaticDishs.add(mStaticDish);
//				
//			}
//			ConnectionPool.release(connection, stmt);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return mStaticDishs;
//		
//	}
//
//	/**
//	 * ����ͳ��
//	 * @param zhouNum  һ���е�����
//	 * @param tongjiNum  һ�������еĵڼ��� 1 - 7
//	 * @return
//	 */
//	public static List<Wage> getWage(int zhouNum, int tongjiNum) {
//		List<Wage> mWageList = new ArrayList<Wage>();
//		String sql = "execute dailyIncome ? , ?";
//		try {
//			Connection connection = ConnectionPool.getConnection();
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			for (int i = zhouNum - tongjiNum; i < zhouNum; i++) {
//				for (int j = 1; j < 8; j++) {
//					pstmt.setInt(1, i);
//					pstmt.setInt(2, j);
//					ResultSet rs = pstmt.executeQuery();
//					if (rs.next()) {
//						Wage wage = new Wage(i, j, rs.getInt(1));
//						mWageList.add(wage);
//					}
//					pstmt.clearParameters();
//					
//				}
//			}
//			ConnectionPool.release(connection, pstmt);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return mWageList;
//	}
	
	
	
	
	
}
