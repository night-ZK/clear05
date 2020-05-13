-- outDay clear
SELECT
	t1.id
	,t1.user_id 用户id
	,t1.create_time 最后任务时间
	,t2.mobile 账号
	,t2.real_name 姓名
	,t2.id_card 身份证
FROM
	(SELECT 
		t1.id
		,t1.user_id
		,t1.create_time
	FROM
		com_user_wallet_detail t1
	WHERE
		t1.remark = '答题奖励'
		GROUP BY user_id 
		HAVING MAX(create_time) < DATE_SUB(CURDATE(),INTERVAL 15 DAY)) t1
	,com_user t2
WHERE
	t1.user_id = t2.id
	
	
-- multiple clear
SELECT 
	id 用户id
	,mobile 账号
	,real_name 姓名
	,id_card 身份证
	,COUNT(mobile_lock) 相同数量
	,mobile_lock 设备锁
FROM
	com_user t1
WHERE
	t1.status = 0
	AND t1.mobile_lock != '' 
	AND t1.mobile_lock IS NOT NULL
	GROUP BY mobile_lock 
	HAVING COUNT(mobile_lock) >= 3
	
-- partner cleaar
EXPLAIN
SELECT 
	t2.user_id 用户id
	,t1.real_name 姓名
	,t1.mobile 账号
	,t1.id_card 身份证
	,t2.basic 基础贡献值
	,t1.agent_type 代理类型
	,CASE t1.agent_type
	WHEN 1 THEN '省'
	WHEN 2 THEN '市'
	WHEN 3 THEN '区'
	ELSE '无'
	END 代理区域
	-- ,IF(t1.agent_type == 1,'省',IF(t1.agent_type == 2,'市','区')) agentName
	,t1.agent_region_id 区域id
	,t3.`name` 区域名称
FROM
	com_user t1
	,com_user_wallet t2
	,com_region t3
WHERE
	t1.id = t2.user_id
	AND t1.agent_type != 0
	AND t2.basic < 348
	AND t1.agent_region_id = t3.id


--insert
UPDATE com_user SET `status` = 1 WHERE id in (2589632, 2589547)


select * from INFORMATION_SCHEMA.columns
where COLUMN_NAME Like '%user_id%' and table_schema = 'vstest';

SELECT *
FROM information_schema.tables
WHERE table_schema = 'vstest'
ORDER BY table_name DESC; 