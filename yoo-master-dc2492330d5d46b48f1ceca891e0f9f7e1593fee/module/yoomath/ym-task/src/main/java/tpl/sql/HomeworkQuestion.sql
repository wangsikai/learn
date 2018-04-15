
## 查询作业习题正确个数，错误个数集合
#macro($ymGetHkQuestion(homeworkIds))
SELECT SUM(a.right_count) right_count,SUM(a.wrong_count) wrong_count,SUM(a.half_wrong_count) as half_wrong_count, a.question_id, q.difficulty,a.homework_id FROM homework_question a
INNER JOIN question q ON q.id = a.question_id
WHERE a.homework_id IN :homeworkIds and a.right_rate is not null
GROUP BY a.question_id
#end

##查询作业对应的知识点正确个数，错误个数集合(新知识点)
#macro($getHkQuestionStat(homeworkId))
SELECT SUM(a.right_count) rightcount,SUM(a.wrong_count) wrongcount,b.knowledge_code FROM homework_question a 
INNER JOIN question_knowledge b ON a.question_id = b.question_id
WHERE a.homework_id = :homeworkId AND a.right_rate IS NOT NULL
GROUP BY b.knowledge_code
#end

#macro($TaskGetQuestion(homeworkId))
SELECT question_id FROM homework_question WHERE status = 0 AND homework_id = :homeworkId ORDER BY sequence ASC
#end
