<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <!--   ${gpsPoint} <br> -->
    
    <script type="text/javascript">
/* 	var jArray = ${ gpsPoint };
 	for ( var i = 0; i < jArray.length; i++) {
		document.writeln(jArray[i].lat + " " + jArray[i].lngt +"<br>");
	}  */
	
    /**
     * 将v值限定在a,b之间，经度使用
     */
    function _getLoop(v, a, b){
        while( v > b){
          v -= b - a
        }
        while(v < a){
          v += b - a
        }
        return v;
    }
	
    /**
     * 将v值限定在a,b之间，纬度使用
     */
    function _getRange(v, a, b){
        if(a != null){
          v = Math.max(v, a);
        }
        if(b != null){
          v = Math.min(v, b);
        }
        return v;
    }
/*     point1.lng = _getLoop(point1.lng, -180, 180);
    point1.lat = _getRange(point1.lat, -74, 74); */
   docment.write(_getLoop(116.33114227578, -180, 180));
   docment.write(_getRange(39.991763862465, -74, 74));

    
    
    </script>
    
  </body>
</html>
