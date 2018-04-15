## 科举考试,根据阶段和学生查询对应的作业
#macro($list(code,type,userId,tag,room))
SELECT * FROM imperial_exam_homework_student 
WHERE activity_code = :code and type =:type and user_id = :userId
#if(tag)
	AND tag = :tag
#end
#if(room)
	AND room = :room
#end
ORDER BY tag desc
#end