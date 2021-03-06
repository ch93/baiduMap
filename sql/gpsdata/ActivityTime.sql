
-- 查找前五所有活动时间
delimiter //
DROP PROCEDURE IF EXISTS ActivityTime;
CREATE PROCEDURE ActivityTime (user_ID int)
BEGIN
-- 需要定义接收游标数据的变量 
  DECLARE a int;

  DECLARE poiTypeNum int;
  -- 遍历数据结束标志
  DECLARE done INT DEFAULT FALSE;
  -- 游标
  DECLARE cur CURSOR FOR SELECT clusterID,COUNT(*) AS num
														FROM clusteredpoint
														WHERE userid = user_ID
														GROUP BY clusterID
														ORDER BY num DESC
														LIMIT 0,5
														;

  -- 将结束标志绑定到游标
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

		SELECT clusterID, poiType, tag,COUNT(*) AS num
		FROM clusteredpoint
		WHERE userid = user_ID
		GROUP BY clusterID
		ORDER BY num DESC
		LIMIT 0,5
		;
  -- 打开游标
  OPEN cur;
  
  -- 开始循环
  read_loop: LOOP
    -- 提取游标里的数据，这里只有一个，多个的话也一样；
    FETCH cur INTO a, poiTypeNum;
    -- 声明结束的时候
    IF done THEN
      LEAVE read_loop;
    END IF;
    -- 这里做你想做的循环的事件
		SELECT clusterID,time_to_sec(date_format(arvT, '%H:%i:%s'))/3600,( time_to_sec(date_format(levT, '%H:%i:%s')) - time_to_sec(date_format(arvT, '%H:%i:%s')) )/3600, poiType, tag
		FROM clusteredpoint
		WHERE userid = user_ID AND clusterID = a AND dayofweek(arvT) != 7 AND dayofweek(arvT) != 1
		ORDER BY iArvT ASC
		;
    

  END LOOP;
  -- 关闭游标
  CLOSE cur;

END
//

CALL ActivityTime (4);