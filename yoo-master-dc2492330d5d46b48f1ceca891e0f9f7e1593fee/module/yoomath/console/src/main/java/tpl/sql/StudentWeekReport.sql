## 临时跑学生周报告要用，后续这个文件要删除
##获取学生指定时间的周报表
#macro($getByUser(userId,startDate,endDate))
	select * from student_week_report 
	where user_id = :userId and start_date = :startDate and end_date = :endDate
#end
