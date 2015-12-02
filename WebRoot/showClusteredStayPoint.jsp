<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="lib/bootstrap/css/bootstrap.min.css">
<script src="lib/jquery-2.1.4.min.js"></script>
<script src="lib/bootstrap/js/bootstrap.min.js"></script>


<title>聚类后停留点显示</title>

<!-- <style type="text/css">
body,html {
	width: 100%;
	height: 100%;
	margin: 0;
	font-family: "微软雅黑";
}

#allmap {
	height: 630px;
	width: 100%;
}

#r-result {
	width: 100%;
}
</style> -->

<!-- laod baidu Map js -->
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=2hHDLHPkjtI66ElRG87t1IBM"></script>
<!-- 加载控制地图范围 -->
<script type="text/javascript"
	src="http://api.map.baidu.com/library/AreaRestriction/1.2/src/AreaRestriction_min.js"></script>
<!-- 鼠标测距 -->
<script type="text/javascript"
	src="http://api.map.baidu.com/library/DistanceTool/1.2/src/DistanceTool_min.js"></script>
	
</head>

<body>


	<div class="container" style="margin-top:5px">

		<div class="row">
			<div id="allmap" class="col-md-11 " style="height:700px"></div>

			<div id="r-result" class="col-md-1 ">

				<div class="btn-group-vertical" style="margin-top: 10px">
					<button type="button" class="btn btn-default"
						onclick="add_overlay();">添加覆盖物</button>
					<button type="button" class="btn btn-default"
						onclick="add_lable();">添加点标注</button>
					<button type="button" class="btn btn-default"
						onclick="addPolyline();">绘 线</button>
					<button type="button" class="btn btn-default"
						onclick="remove_overlay();">删除覆盖物</button>
						
				</div>

				<button type="button" class="btn btn-default"
					style="margin-top: 20px" onclick="CluseterResult();">聚类显示</button>

				<button type="button" class="btn btn-default"
					style="margin-top: 20px" onclick="DistanceMeasure();">鼠标测距</button>

				<p id="outputGPS" style="margin-top: 20px"></p>
				
			</div>

		</div>

	</div>

</body>
</html>


<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("allmap");
	var point = new BMap.Point(116.404, 39.915);
	map.centerAndZoom(point, 14);
	//添加控件和比例尺
	var top_left_control = new BMap.ScaleControl({
		anchor : BMAP_ANCHOR_TOP_LEFT
	});// 左上角，添加比例尺
	var top_left_navigation = new BMap.NavigationControl(); //左上角，添加默认缩放平移控件
	map.addControl(top_left_control);
	map.addControl(top_left_navigation);
	//添加地图类型控件
	map.addControl(new BMap.MapTypeControl());
	//控制地图范围
	/* 	var b = new BMap.Bounds(new BMap.Point(115.430282,39.445587),new BMap.Point(117.513171,41.066884));
	 try {	
	 BMapLib.AreaRestriction.setBounds(map, b);
	 } catch (e) {
	 alert(e);
	 } */
	//获取后台的数据
	var jArray = ${ stayPoint };
	
	var clusterNum = jArray.clusterNum;
	
	jArray = jArray.stayPoint;
	var points = []; // 添加海量点数据
	for (var i = 0; i < jArray.length; i++) {
		points.push(new BMap.Point(jArray[i].lngt, jArray[i].lat));
	}
	//添加覆盖物
	function add_overlay() {
		//var marker = new BMap.Marker(new BMap.Point(116.404, 39.915)); // 创建点

		//map.panTo(new BMap.Point(jArray.lngt, jArray.lat));
		//map.centerAndZoom(new BMap.Point(jArray[0].lngt, jArray[0].lat), 15);
		if (document.createElement('canvas').getContext) { // 判断当前浏览器是否支持绘制海量点

			var options = {
				size : 3,
				shape : BMAP_POINT_SHAPE_CIRCLE,
				color : '#d340c3'
			}
			var pointCollection = new BMap.PointCollection(points, options); // 初始化PointCollection
			pointCollection.addEventListener('click', function(e) {
				//有待优化，查看点详细信息
				//alert("坐标为：" + e.point.lng + "," + e.point.lat); // 监听点击事件
		          for (var int = 1; int < jArray.length - 1; int++) {
						if (e.point.lng == jArray[int].lngt && e.point.lat == jArray[int].lat) {
							var diffTimeB = jArray[int].itime - jArray[int - 1].itime;
							var diffTimeA = jArray[int + 1].itime - jArray[int].itime;
							document.getElementById("outputGPS").innerHTML="坐标为\n" + e.point.lat + "," + e.point.lng;
							alert("ID: " + int 
									+ "\n坐标为：" + e.point.lng + " , " + e.point.lat 
									+ "\n到达日期 ：" + jArray[int].arvT 
									+ "\n离开日期 ：" + jArray[int].levT
									+ "\n停留时间 ：" + Math.round((jArray[int].iLevT - jArray[int].iArvT)*24*60) + " min");  // 监听点击事件
							
							break;
						}
				   }
				
				
			});
			map.addOverlay(pointCollection); // 添加Overlay
			//移动到第一个点
			map.panTo(new BMap.Point(jArray[0].lngt, jArray[0].lat));
		} else {
			alert("请在chrome、safari、IE8+以上浏览器查看本示例");
		}
	}

	//添加点标注
	function add_lable() {
		// 创建文本标注对象
		var opts = {
			offset : new BMap.Size(5, -5)
		} //设置文本偏移量
		for (var i = 0; i < jArray.length; i = i + 1) {
			var label = new BMap.Label("欢迎使用百度地图哦~", opts);
			label.setStyle({
				color : "red",
				fontSize : "6px",
				height : "6px",
				lineHeight : "6px",
				fontFamily : "微软雅黑"
			});
			label.setPosition(new BMap.Point(jArray[i].lngt, jArray[i].lat));
			label.setContent("" + i);
			map.addOverlay(label);
		}
		map.panTo(new BMap.Point(jArray[0].lngt, jArray[0].lat));
	}
	
	//添加 线
	function addPolyline() {
		
		var lpoints = [];
		var options = {
				strokeColor : "blue",
				strokeWeight : 2,
				strokeOpacity : 0.5
			}
		for (var i = 0; i < jArray.length; i++) {
			var j = i + 1;
			//var temp;
			var tempi = i;
				for (; j < jArray.length; j++) {
					if (Math.abs(Math.floor(jArray[j].iArvT) - Math.floor(jArray[i].iArvT) ) >= 1) {
						//temp = j;
						i = j-1;
						break;
					}
					if (j == jArray.length -1 ) {
						//temp = j;
						i = j-1;
						break;
					}
				}
			for (var ii = tempi; ii < j; ii++) {
				lpoints.push(new BMap.Point(jArray[ii].lngt, jArray[ii].lat));
			}
			if (lpoints.length > 1) {
				var mod7 = Math.floor(jArray[tempi].iLevT)%7;
				if ( mod7== 0 || mod7 == 1) {
					var options = {
							strokeColor : "red",
							strokeWeight : 2,
							strokeOpacity : 0.4
						}
 					var polyline = new BMap.Polyline(lpoints, options); //创建折线
					map.addOverlay(polyline);
				} else {
					var options = {
							strokeColor : "blue",
							strokeWeight : 2,
							strokeOpacity : 0.4
						}
 					var polyline = new BMap.Polyline(lpoints, options); //创建折线
					map.addOverlay(polyline); 
				}

			}
			lpoints.length = 0;
			
		}

		

	}

	//清除覆盖物
	function remove_overlay() {
		map.clearOverlays();
	}
	
	//平面距离测量
	function DistanceMeasure(){
		var myDis = new BMapLib.DistanceTool(map);
		myDis.open();  //开启鼠标测距
	}
	
	//聚类之后显示
	function CluseterResult() {
		
/*  		$.get("/baiduMap/Test",function(data, status){
			alert("Data: " + data + "\nStatus: " + status);
		}); */
		
		
		var tArray = new Array();   //先声明一维
        for(var k=0; k<clusterNum; k++){      //一维长度为i,i为变量，可以根据实际情况改变
			tArray[k]=new Array();    //声明二维，每一个一维数组里面的一个元素都是一个数组；
		}
		//var indexArray = new Array();
		var index = 0;
		for ( var i = 0; i < jArray.length; i++) {
			var xIndex = jArray[i].clusterID;
			var xIndexNext = xIndex;
			if (i < jArray.length - 1) {
				xIndexNext = jArray[i+1].clusterID;
			}
			
			if (xIndex == xIndexNext) {
				tArray[xIndex][index] = jArray[i];
				index++;
			} else {
			    tArray[xIndex][index++] = jArray[i];
				index = 0;
			}
		}
		if (document.createElement('canvas').getContext) { // 判断当前浏览器是否支持绘制海量点
		var colors=new Array("green","red","blue", "yellow","black","grey","purple",
		                     "navy","jade","silver", "gunmetal","gold","peach","camel");
			for ( var int2 = 0; int2 < clusterNum; int2++) {
				var pointsList = tArray[int2];
				var points = [];
				for ( var int3 = 0; int3 < pointsList.length; int3++) {
					points.push(new BMap.Point(pointsList[int3].lngt, pointsList[int3].lat));
				}
				var options = {
					size : 3,
					shape : BMAP_POINT_SHAPE_CIRCLE,
					color : colors[int2%14]
				}
				if ( points.length < 3) {
					continue;
				}
				var pointCollection = new BMap.PointCollection(points, options); // 初始化PointCollection
							pointCollection.addEventListener('click', function(e) {
				//有待优化，查看点详细信息
				//alert("坐标为：" + e.point.lng + "," + e.point.lat); // 监听点击事件
		          for (var int = 1; int < jArray.length - 1; int++) {
						if (e.point.lng == jArray[int].lngt && e.point.lat == jArray[int].lat) {
						
							document.getElementById("outputGPS").innerHTML="坐标为\n" + e.point.lat + "," + e.point.lng;
							alert(
							        "聚类号：" + jArray[int].clusterID
							        + "\n聚类号内的点数：" + tArray[jArray[int].clusterID].length
							        +"\nID: " + int 
									+ "\n坐标为：" + e.point.lng + " , " + e.point.lat 
									+ "\n到达日期 ：" + jArray[int].arvT 
									+ "\n离开日期 ：" + jArray[int].levT
									+ "\n停留时间 ：" + Math.round((jArray[int].iLevT - jArray[int].iArvT)*24*60) + " min");  // 监听点击事件
							
							break;
						}
				   }
			});
				map.addOverlay(pointCollection); // 添加Overlay
			}
			
			//移动到第一个点
			map.panTo(new BMap.Point(jArray[0].lngt, jArray[0].lat));
		} else {
			alert("请在chrome、safari、IE8+以上浏览器查看本示例");
		}
	}
	
</script>
