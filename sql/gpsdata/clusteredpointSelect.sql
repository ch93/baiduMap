-- 查看总记录数
SELECT COUNT(*)
FROM clusteredpoint
;

-- 按poiType分组 按 num 排序
SELECT poiType, COUNT(*) AS num
FROM clusteredpoint
GROUP BY poiType
ORDER BY num DESC
limit 0,15
;


SELECT poiType, tag, COUNT(*) AS num
FROM clusteredpoint
WHERE poiType = '房地产'
GROUP BY tag
ORDER BY num DESC
;



-- 按 tag 分组 按 tag 排序
SELECT tag, COUNT(*) AS num
FROM clusteredpoint
GROUP BY tag
ORDER BY tag DESC
;


-- 按 tag 分组 按 num 排序
SELECT tag, COUNT(*) AS num
FROM clusteredpoint
GROUP BY tag
ORDER BY num DESC
;

delete from clusteredpoint where poiType IS NULL;



-- 查看用户的最大簇号，根据簇号分组
delimiter //
DROP PROCEDURE IF EXISTS groupPoint;
-- clusterIDNum 用户号
CREATE PROCEDURE groupPoint( clusterIDNum int) 
BEGIN

SELECT COUNT(*), MAX(clusterID) 
FROM clusteredpoint
WHERE userid = clusterIDNum
;


SELECT clusterID, poiType, tag,COUNT(*) AS num
FROM clusteredpoint
WHERE userid = clusterIDNum
GROUP BY clusterID
ORDER BY num DESC
LIMIT 0,15
;


SELECT *
FROM clusteredpoint
WHERE userid = clusterIDNum
;
END
//

CALL groupPoint (17) 

-- test time 
SELECT date_format('2009-04-13 22:51:45', '%H:%i:%s');
SELECT date_format('2009-04-13 22:51:45', '%h:%i:%s');
SELECT date_format('2008-11-05 00:25:51', '%H:%i:%s');
SELECT date_format('2008-11-05 00:25:51', '%h:%i:%s');

SELECT dayofweek('2015-12-05 00:25:51');
SELECT dayofweek('2015-12-06 00:25:51');
SELECT dayofweek('2015-12-07 00:25:51');

time_to_sec('01:00:05')


-- 显示活动的时间
delimiter //
DROP PROCEDURE IF EXISTS findThirdPlace;
-- use_id 用户
-- clusterIDNum 聚类号
CREATE PROCEDURE findThirdPlace(use_id int, clusterIDNum int)
BEGIN

SELECT clusterID,time_to_sec(date_format(arvT, '%H:%i:%s'))/3600,( time_to_sec(date_format(levT, '%H:%i:%s')) - time_to_sec(date_format(arvT, '%H:%i:%s')) )/3600, poiType, tag
FROM clusteredpoint
WHERE userid = use_id AND clusterID = clusterIDNum AND dayofweek(arvT) != 7 AND dayofweek(arvT) != 1
ORDER BY iArvT ASC
;

SELECT clusterID,date_format(arvT, '%H:%i:%s'),( time_to_sec(date_format(levT, '%H:%i:%s')) - time_to_sec(date_format(arvT, '%H:%i:%s')) )/3600, poiType, tag
FROM clusteredpoint
WHERE userid = use_id AND clusterID = clusterIDNum AND dayofweek(arvT) != 7 AND dayofweek(arvT) != 1
ORDER BY iArvT ASC
;

SELECT clusterID, arvT, levT, poiType, tag
FROM clusteredpoint
WHERE userid = use_id AND clusterID = clusterIDNum AND dayofweek(arvT) != 7 AND dayofweek(arvT) != 1
ORDER BY iArvT ASC
;
END
//


CALL groupPoint (3) ;

CALL findThirdPlace(3, 23);




SELECT clusterID, arvT, levT, poiType, tag
FROM clusteredpoint
WHERE userid = 4 AND clusterID = 144 AND dayofweek(arvT) != 7 AND dayofweek(arvT) != 1
ORDER BY iArvT ASC
;




