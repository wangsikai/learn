##获取教材历史操作记录(除本人创建外的记录)
#macro($findList(teachAssistId))
	SELECT * FROM teachassist_history WHERE teachassist_id = :teachAssistId
	ORDER BY create_at asc limit 0,11
#end

##第一条记录
#macro($findFirstLog(teachAssistId))
	SELECT * FROM teachassist_history WHERE teachassist_id = :teachAssistId AND TYPE =1 order by create_at limit 0,1
#end
