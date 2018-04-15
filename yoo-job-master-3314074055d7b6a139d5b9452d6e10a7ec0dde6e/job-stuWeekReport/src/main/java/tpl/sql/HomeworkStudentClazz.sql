## 查找学生班级
#macro($listCurrentClazzsHasTeacher(studentId))
SELECT t2.id 
FROM 
	homework_student_class t1 INNER JOIN homework_class t2 ON t1.class_id = t2.id
WHERE 
	t1.student_id = :studentId 
AND t1.status = 0 
AND t2.status = 0
AND t2.teacher_id > 0
ORDER BY t1.join_at DESC
#end