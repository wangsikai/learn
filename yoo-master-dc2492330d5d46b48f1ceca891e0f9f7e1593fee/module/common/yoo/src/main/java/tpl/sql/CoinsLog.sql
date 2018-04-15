##查询最近一年金币日志
#macro($queryCoinsLog(userId,ruleCode,endTime,startTime))
SELECT * FROM coins_log WHERE user_id = :userId AND status = 0
	#if(ruleCode)
	AND rule_code < :ruleCode
	#end
	#if(startTime)
	AND create_at  >= :startTime
	#end
	#if(endTime)
	AND create_at < :endTime
	#end
	ORDER BY create_at DESC
#end

##用户ID，成长值规则获取金币记录
#macro($find(ruleCode,userId,bizId))
SELECT * FROM coins_log WHERE rule_code = :ruleCode AND user_id = :userId
#if(bizId)
AND biz_id=:bizId
#end
#end

##更新金币记录，学生做多条题目只需保存一条记录
#macro($updateCoinsValue(userId,ruleCode,beginTime,endTime))
UPDATE coins_log SET coins_value = coins_value+1
WHERE user_id=:userId AND rule_code = :ruleCode
AND create_at >= :beginTime AND create_at <= :endTime
#end

##查询今天金币日志 action获取的金币数量
#macro($getCoinsCountByAction(ruleCode,startTime,endTime,userId))
SELECT SUM(coins_value) FROM coins_log  WHERE create_at> :startTime  AND create_at<:endTime AND rule_code=:ruleCode AND user_id=:userId
#end


##某种growth code 金币操作次数  用户
#macro($countActionByUser(code,userId,start,end))
SELECT count(*)  FROM coins_log where rule_code=:code and user_id=:userId
#if(start)
AND create_at>=:start
#end
#if(end)
AND create_at<=:end
#end
#end