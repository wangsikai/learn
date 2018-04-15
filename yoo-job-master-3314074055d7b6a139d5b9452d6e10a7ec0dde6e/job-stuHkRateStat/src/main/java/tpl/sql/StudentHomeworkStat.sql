## 查询学生作业正确率统计
#macro($getAvgRate(studentId))
	select ROUND(AVG(a.right_rate))
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0 and a.status = 2
	 and a.student_id = :studentId
#end