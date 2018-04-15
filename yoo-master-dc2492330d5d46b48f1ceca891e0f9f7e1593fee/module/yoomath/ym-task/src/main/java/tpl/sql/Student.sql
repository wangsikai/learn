##分页获取学生
#macro($taskGetAllByPage())
SELECT id FROM student WHERE id < :next ORDER BY id DESC
#end

## 根据班级id查找班级所有学生
#macro($taskListAllStudents(classId))
select t.* from student t
inner join homework_student_class hc on hc.student_id=t.id
AND hc.class_id = :classId AND hc.status = 0
#end
