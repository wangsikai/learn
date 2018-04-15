## 查询用户相关班级的排名情况
#macro($findList(classIds,startDate,endDate,userId))
SELECT * FROM student_class_week_report 
WHERE clazz_id in :classIds
#if(startDate)
	and start_date = :startDate
#end
#if(endDate)
	and end_date = :endDate
#end
#if(userId)
	and user_id = :userId
#end
order by clazz_id
#end