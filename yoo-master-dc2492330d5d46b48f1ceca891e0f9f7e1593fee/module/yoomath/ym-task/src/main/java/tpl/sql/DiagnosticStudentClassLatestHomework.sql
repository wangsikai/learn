##获取学生诊断对象
#macro($getDiagnosticStuClassLatestHk(studentId,homeworkId))
SELECT * FROM diagno_stu_class_latest_hk WHERE student_id =:studentId
	#if(homeworkId)
		AND homework_id =:homeworkId  
	#end
#end

##删除不是最新你的学生诊断对象
#macro($deleteDiagnosticStuClassLatestHk(startTime,studentId))
delete FROM diagno_stu_class_latest_hk WHERE student_id =:studentId and start_time < :startTime
#end


##根据班级和学生查询最新的30条
#macro($taskGetLast(studentId,classId))
SELECT * FROM diagno_stu_class_latest_hk WHERE student_id =:studentId and class_id = :classId order by start_time desc limit 0,30
#end

#macro($taskDelStuClassLatestHk(classId,studentId))
DELETE FROM diagno_stu_class_latest_hk WHERE class_id =:classId 
#if(studentId)
	and student_id = :studentId
#end
#end
