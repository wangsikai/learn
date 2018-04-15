##学生题目知识点统计(新知识点)
#macro($getStuQuestionStat(homeworkId))
	SELECT a.student_id,b.result,d.knowledge_code FROM student_homework a 
	INNER JOIN student_homework_question b ON a.id = b.student_homework_id 
	INNER JOIN homework_question c ON b.question_id = c.question_id AND a.homework_id = c.homework_id
	INNER JOIN question_knowledge d ON b.question_id = d.question_id
	AND a.homework_id = :homeworkId AND a.submit_at IS NOT NULL AND a.stu_submit_at IS NOT NULL
	AND b.result IS NOT NULL
	ORDER BY student_id 
#end

##查询学生作业情况(新知识点)
#macro($getHkStuClazzNewKp(studetnId,classId,knowledgeCode))
SELECT * FROM homework_student_class_knowpoint_stat 
WHERE student_id =:studetnId AND class_id=:classId AND knowpoint_code =:knowledgeCode
#end

##按正确率排序(新知识点)
#macro($getHkStuClazzNewKpOrderByRate(knowpoint,classId))
SELECT * FROM homework_student_class_knowpoint_stat 
WHERE class_id=:classId AND knowpoint_code =:knowpoint 
order by right_rate desc
#end


##删除学生的知识点统计(新知识点)
#macro($deleteNewAll())
DELETE FROM homework_student_class_knowpoint_stat
#end
