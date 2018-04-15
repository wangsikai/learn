## 统计个数，最多只能三次
#macro($logCount(ruleCode,userId,biz,bizId))
	SELECT count(id) FROM coins_log 
	where rule_code = :ruleCode and user_id = :userId
	#if(biz)
		and biz = :biz
	#end
	#if(bizId)
		and biz_id = :bizId
	#end
#end