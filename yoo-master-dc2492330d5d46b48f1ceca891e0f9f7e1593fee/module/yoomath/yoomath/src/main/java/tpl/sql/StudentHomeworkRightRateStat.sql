##获取正确率列表
#macro(findList(userId,startTime,endTime))
	select * from student_homework_rightrate_stat 
	where user_id = :userId and statistics_time >= :startTime and statistics_time < :endTime
	order by statistics_time
#end