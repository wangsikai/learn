## 查询会话
#macro($querySession(startTime,endTime,userType,phaseCode))
SELECT a.update_at,a.user_id,b.name,b.user_type,c.phase_code,c.sex,c.school_id,a.create_at FROM yoomath_customer_service_session a 
INNER JOIN USER b ON a.user_id = b.id 
INNER JOIN teacher c ON a.user_id = c.id
WHERE a.status != 2
	#if(userType)
	AND b.user_type = :userType
	#end
	#if(phaseCode)
	AND c.phase_code = :phaseCode
	#end
	#if(startTime)
	AND a.update_at >= :startTime
	#end
	#if(endTime)
	AND a.update_at < :endTime
	#end
UNION
SELECT a.update_at,a.user_id,b.name,b.user_type,c.phase_code,c.sex,c.school_id,a.create_at FROM yoomath_customer_service_session a 
INNER JOIN USER b ON a.user_id = b.id 
INNER JOIN student c ON a.user_id = c.id
WHERE a.status != 2
	#if(userType)
	AND b.user_type = :userType
	#end
	#if(phaseCode)
	AND c.phase_code = :phaseCode
	#end
	#if(startTime)
	AND a.update_at >= :startTime
	#end
	#if(endTime)
	AND a.update_at < :endTime
	#end
order by update_at desc
#end

## 查询历史
#macro($queryLog(keyStr,userId,startTime,endTime))
SELECT b.name username,a.user_id,a.create_at,a.content,c.name customer,a.from_user,a.img_id,a.customer_service_id FROM yoomath_customer_service_log a
INNER JOIN user b ON a.user_id = b.id
INNER JOIN yoomath_customer_service c ON a.customer_service_id = c.id
WHERE a.status != 2 AND a.user_id =:userId
	#if(keyStr)
	AND a.content LIKE :keyStr
	#end
	#if(startTime)
	AND a.create_at >= :startTime
	#end
	#if(endTime)
	AND a.create_at < :endTime
	#end
ORDER BY a.create_at ASC
#end

## 查询历史个数
#macro($getLogCount(keyStr,userId,startTime,endTime))
SELECT count(a.id) FROM yoomath_customer_service_log a
INNER JOIN user b ON a.user_id = b.id
WHERE a.status != 2 AND a.user_id =:userId
	#if(keyStr)
	AND a.content LIKE :keyStr
	#end
	#if(startTime)
	AND a.create_at >= :startTime
	#end
	#if(endTime)
	AND a.create_at < :endTime
	#end
#end

## 查找所有的对话信息
#macro($zycFindAll())
SELECT * FROM yoomath_customer_service_session t WHERE t.status =0 ORDER BY t.update_at DESC
#end

## 根据用户id得到对话信息
#macro($zycGetByUser(userId))
SELECT * FROM yoomath_customer_service_session t WHERE t.user_id = :userId
#end
