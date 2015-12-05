调试端接口

//显示原始GPS点
format=1 本地算法转 GPS 
format=2 百度地图api
userid=1 userid为用户名  72
fileName 为文件夹名     72
 
http://localhost:8080/baiduMap/ShowPoint?userid=0&fileName=20081026134407


//显示GPS停留点
userid=1 userid为用户名
http://localhost:8080/baiduMap/ShowStayPoint?userid=4


//显示聚类后的GPS停留点
userid=1 userid为用户名
http://localhost:8080/baiduMap/ShowClusteredStayPoint?userid=4






