
-- 创建 gpsdata 数据库
-- CREATE DATABASE gpsdata;

-- 创建 clusteredpoint表
DROP TABLE IF EXISTS clusteredpoint;
CREATE TABLE clusteredpoint (
  userid int(11) DEFAULT NULL,
  lat double DEFAULT NULL,
  lngt double DEFAULT NULL,
  arvT timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  levT timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  iArvT double DEFAULT NULL,
  iLevT double DEFAULT NULL,
  clusterID int(11) DEFAULT NULL,
  poiType varchar(255) DEFAULT NULL,
  tag varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;
