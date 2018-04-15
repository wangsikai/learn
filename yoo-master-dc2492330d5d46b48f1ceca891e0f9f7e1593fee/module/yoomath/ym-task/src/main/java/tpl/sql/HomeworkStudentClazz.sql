#macro($taskListStudent(classId))
SELECT student_id FROM homework_student_class WHERE status = 0 AND class_id = :classId
#end

#macro($queryClazzIds(studentId,classId))
SELECT class_id FROM homework_student_class WHERE status = 0 
AND student_id = :studentId
#if(classId)
	and class_id != :classId
#end
#end

## 班级里面的有效学生
#macro($TaskListClassStudents(classId))
SELECT student_id FROM homework_student_class WHERE class_id = :classId AND status = 0 ORDER BY join_at DESC 
#end

## 查询班级学生信息
#macro($getByStudentExit(classId, studentId))
SELECT * FROM homework_student_class 
WHERE class_id = :classId AND student_id = :studentId
order by exit_at DESC
limit 1
#end