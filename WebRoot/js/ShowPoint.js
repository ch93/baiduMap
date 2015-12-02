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
	          for (var int = 1; int < jArray.length - 1; int++) {
				if (e.point.lng == jArray[int].lngt && e.point.lat == jArray[int].lat) {
					var diffTimeB = jArray[int].itime - jArray[int - 1].itime;
					var diffTimeA = jArray[int + 1].itime - jArray[int].itime;
					alert("ID: " + int + "\n坐标为：" + e.point.lng + "," + e.point.lat + "\n前一坐标时间间隔 ：" + Math.round(diffTimeB*24*3600) +"s "+ "\n后一坐标时间间隔 ：" +Math.round(diffTimeA*24*3600) +"s ");  // 监听点击事件
					break;
				}
			}
	          
	          
	        });
	        map.addOverlay(pointCollection);  // 添加Overlay
	        
		} else {
        alert("请在chrome、safari、IE8+以上浏览器查看本示例");
        }
	}
	
	//添加点标注
	function add_lable(){
		//var jArray = ${ gpsPoint };
		   for (var i = 0; i < jArray.length; i=i+5) {
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
	
	//平面距离测量
	function DistanceMeasure(){
		var myDis = new BMapLib.DistanceTool(map);
		myDis.open();  //开启鼠标测距
	}
	
	function getValue() {
	    $.get("/ShowClusteredStayPoint",function(data, status){
			alert("Data: " + data + "\nStatus: " + status);
		} );
	}
	
	
	