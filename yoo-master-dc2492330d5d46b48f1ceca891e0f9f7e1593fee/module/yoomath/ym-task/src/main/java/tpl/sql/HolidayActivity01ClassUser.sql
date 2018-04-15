##分页获取假期班级用户
#macro($findClassUserList())
SELECT id FROM holiday_activity_01_class_user WHERE id < :next ORDER BY id DESC
#end

##批量获取假期班级用户
#macro($mgetHolidayActivity01ClassUser(ids))
SELECT * FROM holiday_activity_01_class_user WHERE id in (:ids)
#end