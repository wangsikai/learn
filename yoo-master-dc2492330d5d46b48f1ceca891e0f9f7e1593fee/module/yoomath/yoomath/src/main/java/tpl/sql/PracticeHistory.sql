##通过系统业务KEY和业务ID获取练习历史对象
#macro($getByBizId(biz,bizId))
SELECT * FROM practice_history WHERE biz =:biz AND biz_id =:bizId
#end

##获取练习历史
#macro($queryHistory(biz,cursorDate,userId))
SELECT * FROM practice_history WHERE user_id = :userId
#if(biz)
AND biz =:biz
#end
AND create_at < :cursorDate
ORDER BY create_at DESC
#end

##获取用户练习历史数量
#macro($count(userId))
SELECT count(id) FROM practice_history WHERE user_id = :userId
#end