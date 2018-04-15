## 根据班级获得全部信息
#macro($findHolidayActivity01ClassUser(classId, activityCode))
SELECT * FROM holiday_activity_01_class_user 
WHERE class_id = :classId
#if(activityCode)
	AND activity_code = :activityCode
#end
order by submit_rate desc
#end