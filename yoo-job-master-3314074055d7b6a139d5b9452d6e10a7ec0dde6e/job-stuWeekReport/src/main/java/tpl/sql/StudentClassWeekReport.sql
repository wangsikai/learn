#macro($getByUser(userId,classId,startDate,endDate))
	select * from student_class_week_report 
	where clazz_id = :classId and user_id = :userId 
	and start_date = :startDate and end_date = :endDate
#end

#macro($findList(classIds,startDate,endDate))
	select * from student_class_week_report 
	where clazz_id in :classIds and start_date = :startDate and end_date = :endDate 
	ORDER BY right_rate_rank
#end


