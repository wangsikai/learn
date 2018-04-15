## 临时跑学生周报告要用，后续这个文件要删除
#macro($getByUser(userId,classId,startDate,endDate))
	select * from student_class_week_report 
	where clazz_id = :classId and user_id = :userId 
	and start_date = :startDate and end_date = :endDate
#end

#macro($weekreportfindList(classIds,startDate,endDate))
	select * from student_class_week_report 
	where clazz_id in :classIds and start_date = :startDate and end_date = :endDate 
	ORDER BY right_rate_rank
#end


