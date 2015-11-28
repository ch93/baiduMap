<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>转百度坐标后的点</title>

<style type="text/css">
		body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
		#allmap{height:630px;width:100%;}
		#r-result{width:100%;}
	</style>
	
	<!-- laod baidu Map js -->
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2hHDLHPkjtI66ElRG87t1IBM"></script>
	<!-- 加载控制地图范围 -->
	<script type="text/javascript" src="http://api.map.baidu.com/library/AreaRestriction/1.2/src/AreaRestriction_min.js"></script>
	<title>添加/删除覆盖物</title>

</head>
<body>

	<div id="allmap">
	
	</div>
	
	<div id="r-result">
		<input type="button" onclick="add_overlay();" value="添加覆盖物" />
		<input type="button" onclick="add_lable();" value="添加点标注" />
		<input type="button" onclick="addPolyline();" value="绘线" />
		<input type="button" onclick="remove_overlay();" value="删除覆盖物" />
	</div>

</body>
</html>


<script type="text/javascript">

	// 百度地图API功能
	var map = new BMap.Map("allmap");
	var point = new BMap.Point(116.404, 39.915);
	map.centerAndZoom(point, 14);
	//添加控件和比例尺
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
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
	var jArray = ${ gpsPoint };
	//jArray = jArray.stayPoint;
	var points = [];  // 添加海量点数据
	for (var i = 0; i < jArray.length; i++) {
        points.push(new BMap.Point(jArray[i].lngt, jArray[i].lat));
      }
	//添加覆盖物
	function add_overlay(){
	//var marker = new BMap.Marker(new BMap.Point(116.404, 39.915)); // 创建点
	//var jArray = ${ gpsPoint };
	map.panTo(new BMap.Point(jArray[0].lngt, jArray[0].lat));
	//map.centerAndZoom(new BMap.Point(jArray[0].lngt, jArray[0].lat), 15);
		if (document.createElement('canvas').getContext) {  // 判断当前浏览器是否支持绘制海量点
/* 		    var points = [];  // 添加海量点数据
			for (var i = 0; i < jArray.length; i++) {

	          points.push(new BMap.Point(jArray[i].lngt, jArray[i].lat));
	        } */
			
	        var options = {
	            size: BMAP_POINT_SIZE_SMALL,
	            shape: BMAP_POINT_SHAPE_CIRCLE,
	            color: '#d340c3'
	        }
	        var pointCollection = new BMap.PointCollection(points, options);  // 初始化PointCollection
	        pointCollection.addEventListener('click', function (e) {
	        	//有待优化，查看点详细信息
	          alert("坐标为：" + e.point.lng + "," + e.point.lat);  // 监听点击事件
	        });
	        map.addOverlay(pointCollection);  // 添加Overlay
	        
		} else {
        alert("请在chrome、safari、IE8+以上浏览器查看本示例");
        }
	}
	
	//添加点标注
	function add_lable(){
		//var jArray = ${ gpsPoint };
		   for (var i = 0; i < jArray.length; i=i+50) {
				// 创建文本标注对象
				var opts = { offset   : new BMap.Size(5, -5) }   //设置文本偏移量
			    var label = new BMap.Label("欢迎使用百度地图哦~", opts);  
				label.setStyle({
					 color : "red",
					 fontSize : "6px",
					 height : "6px",
					 lineHeight : "6px",
					 fontFamily:"微软雅黑"
				 });
		          label.setPosition(new BMap.Point(jArray[i].lngt, jArray[i].lat));
				  label.setContent(""+i);
				  map.addOverlay(label);
				  map.panTo(new BMap.Point(jArray[0].lngt, jArray[0].lat));
		        }
	}
	//添加 线
	function addPolyline() {
        var options = {
        		strokeColor:"blue", 
        		strokeWeight:2,
        		strokeOpacity:0.5
	        }
		var polyline = new BMap.Polyline(points, options);   //创建折线
		map.addOverlay(polyline);
	}
	//清除覆盖物
	function remove_overlay(){
		map.clearOverlays();         
	}
</script>
