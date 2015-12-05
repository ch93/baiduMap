package com.ch.module;

import java.io.IOException;
import java.util.List;

import com.ch.dbscan.Dbscan;
import com.ch.dbscan.Point;
import com.ch.dbscan.Utility;
import com.ch.loaddata.MyDataBase;


public class GeocodingThread extends MutiThread{
	
	private List<Object> filelist;
	

	public GeocodingThread(List<Object> filelist, int CurrentT) {
		super(filelist, CurrentT);
		this.filelist = filelist;
	}

	
	@Override
	public void doProcess() {
		// TODO Auto-generated method stub
		for (int i = 0; i < filelist.size(); i++) {	
			
			Dbscan dbscan = new Dbscan();
			try {
				String path = (String)filelist.get(i);
				dbscan.applyDbscan(path);
				Utility.display(dbscan.resultList);
				Utility.saveResult(dbscan.resultList, path.replace("stayPoint.json", "clusteredStayPoint.json"));
				if (dbscan.resultList.size() == 0) {
					continue;
				}
				Geocoding geocoding = new Geocoding();
				List<List<Point>> resultList = geocoding.lableThePOI(dbscan.resultList);
				MyDataBase dataBase = new MyDataBase();
				dataBase.insertClusteredPoint(resultList);
				super.setCurrentN(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		

		
	}
	
	

}
