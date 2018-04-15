##获取最大的
#macro($maxCode())
	select max(code) from imperial_exam_activity
#end

##删除对应的code
#macro($deleteByCode(code))
	delete from imperial_exam_activity where code = :code
#end