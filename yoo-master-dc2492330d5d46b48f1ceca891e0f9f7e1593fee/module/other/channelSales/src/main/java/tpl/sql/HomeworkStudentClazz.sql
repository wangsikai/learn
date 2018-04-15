## 查找班级所有学生
#macro($csListAllStudents(classId))
select t.* from student t
inner join homework_student_class hsc on hsc.student_id=t.id
AND hsc.class_id = :classId AND status = 0
order by t.name ASC
#end

## 查找学生班级
#macro($csFindByStudentIds(studentIds))
SELECT t1.* 
FROM 
	homework_student_class t1 INNER JOIN homework_class t2 ON t1.class_id = t2.id
WHERE 
	t1.student_id in (:studentIds) 
AND t1.status = 0
AND t2.status = 0
#end