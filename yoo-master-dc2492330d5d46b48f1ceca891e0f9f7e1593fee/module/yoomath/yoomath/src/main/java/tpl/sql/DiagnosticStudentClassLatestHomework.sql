##根据学生班级查询最近30次作业的统计数据
#macro($queryStat(studentId,classId,times))
SELECT * FROM (
	SELECT * FROM diagno_stu_class_latest_hk WHERE student_id =:studentId AND class_id = :classId 
	ORDER BY start_time DESC
	limit 0,:times
) t order by t.start_time
#end
