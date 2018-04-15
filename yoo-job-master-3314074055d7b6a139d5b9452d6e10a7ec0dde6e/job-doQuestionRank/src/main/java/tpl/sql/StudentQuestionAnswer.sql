#macro($taskStaticDoQuestionStudent(startDate,endDate,userIds,result))
SELECT * FROM student_question_answer 
WHERE create_at >= :startDate 
AND create_at <= :endDate
AND student_id IN (:userIds) 
#if(result)
	AND result = :result
#end
GROUP BY student_id,result,question_id
#end