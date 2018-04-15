##用户ID，成长值规则获取成长值记录
#macro($find(ruleCode,userId))
SELECT * FROM growth_log WHERE rule_code = :ruleCode AND user_id = :userId
#end

##查询最近一个月成长日志
#macro($queryGrowLog(userId,ruleCode,startTime,endTime))
SELECT * FROM growth_log WHERE user_id = :userId AND status = 0
	#if(ruleCode)
	AND rule_code = :ruleCode
	#end
	#if(startTime)
	AND create_at  >= :startTime
	#end
	#if(endTime)
	AND create_at < :endTime
	#end 
	ORDER BY create_at DESC
#end

##判断用户今天有没有签过到
#macro($queryCheck(userId,ruleCode,beginTime,endTime))
SELECT count(1) FROM growth_log WHERE user_id = :userId AND status = 0 AND rule_code = :ruleCode AND create_at  >= :beginTime AND create_at < :endTime
#end

##更新成长记录，学生做多条题目只需保存一条记录
#macro($updateGrowthValue(userId,ruleCode,beginTime,endTime))
UPDATE growth_log SET growth_value = growth_value+1
WHERE user_id=:userId AND rule_code = :ruleCode
AND create_at >= :beginTime AND create_at <= :endTime
#end


##某种growth code 操作次数  用户
#macro($countActionByUser(code,userId,start,end))
SELECT count(*)  FROM growth_log where rule_code=:code and user_id=:userId
#if(start)
AND create_at>=:start
#end
#if(end)
AND create_at<=:end
#end
#end

##用户ID，成长值规则获取最新一条成长签到记录
#macro($getLastestCheckIn(ruleCode,userId))
SELECT * FROM growth_log WHERE id = (SELECT MAX(id) FROM growth_log WHERE user_id = :userId AND rule_code = :ruleCode)
#end