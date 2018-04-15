## 给教师打标签的人数
#macro($numberOfSetTag(teacherId))
SELECT count(DISTINCT student_id) AS count
FROM teachersday_activity_01_stu_tag_tea
WHERE teacher_id = :teacherId
#end

## 查询学生对该老师标签的code集合
#macro($findTagList(studentId,teacherId))
	select code_tag from teachersday_activity_01_stu_tag_tea 
	where student_id = :studentId and teacher_id = :teacherId
#end
