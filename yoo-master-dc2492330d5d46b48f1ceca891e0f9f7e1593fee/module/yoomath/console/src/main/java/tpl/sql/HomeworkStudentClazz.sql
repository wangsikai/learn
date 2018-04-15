#macro($zycGetByClazzId(classId,studentId))
SELECT t.* FROM homework_student_class t, homework_class h WHERE
t.class_id = h.id AND h.id = :classId and student_id = :studentId
#end

##删除班级里的学生
#macro($delStudent(clazzId,studentId))
update homework_student_class
set status = 2
where class_id =:clazzId and student_id = :studentId
#end

## 临时跑学生周报告要用，后续这个文件要删除
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