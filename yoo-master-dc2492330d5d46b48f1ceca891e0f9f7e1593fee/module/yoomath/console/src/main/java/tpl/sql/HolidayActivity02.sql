##获取最大的
#macro($maxCode())
	select max(code) from holiday_activity_02
#end

##删除对应的code
#macro($deleteByCode(code))
	delete from holiday_activity_02 where code = :code
#end