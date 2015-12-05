-- 按poiType分组 按 num 排序
SELECT poiType, COUNT(*) AS num
FROM clusteredpoint
GROUP BY poiType
ORDER BY num DESC
limit 0,15
;

-- 查找前十所有活动
delimiter //
DROP PROCEDURE IF EXISTS allActivity;
CREATE PROCEDURE allActivity ()
BEGIN
-- 需要定义接收游标数据的变量 
  DECLARE a varchar(255);

  DECLARE poiTypeNum int;
  -- 遍历数据结束标志
  DECLARE done INT DEFAULT FALSE;
  -- 游标
  DECLARE cur CURSOR FOR SELECT poiType, COUNT(*) AS num
																FROM clusteredpoint
																GROUP BY poiType
																ORDER BY num DESC
																limit 0,15
																;

  -- 将结束标志绑定到游标
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
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
		SELECT poiType, tag, COUNT(*) AS num
		FROM clusteredpoint
		WHERE poiType = a
		GROUP BY tag
		ORDER BY num DESC
		;
    

  END LOOP;
  -- 关闭游标
  CLOSE cur;

END
//

CALL allActivity ();