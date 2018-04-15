#macro($taskStaticDoQuestionStudentStat(startAt,userIds))
SELECT COUNT(id) cou,student_id,result FROM student_question_answer 
WHERE create_at >= :startAt AND student_id IN (:userIds) GROUP BY student_id,result
#end


##学生做题自然月统计(近6个月)
#macro($taskStaticDoQuestionStudentStat2(startTime,endTime,studentIds))
SELECT COUNT(id) cou,student_id,result FROM student_question_answer 
WHERE create_at >= :startTime and create_at <:endTime
AND student_id IN (:studentIds) GROUP BY student_id,result
#end

##学生做题知识点统计(近6个月)
#macro($taskStaticDoQuestionStudentKpStat(startTime,endTime,studentIds))
SELECT a.student_id,b.knowledge_code,a.result,COUNT(a.id) cou FROM student_question_answer a  
INNER JOIN question_knowledge b ON a.question_id = b.question_id
AND a.create_at >= :startTime and a.create_at <:endTime
AND student_id IN (:studentIds)
GROUP BY a.student_id,b.knowledge_code,a.result
#end

##获得上次对此题的答题情况
#macro($getLatestQuestionAnswer(questionId,studentId,sqaId))
SELECT t.* FROM student_question_answer t WHERE t.question_id = :questionId AND t.student_id = :studentId
AND t.id < :sqaId
ORDER BY t.id DESC
LIMIT 1
#end