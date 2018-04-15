#macro($getHomeworkQuestion(homeworkId,questionId))
SELECT * FROM homework_question WHERE status = 0 
AND homework_id = :homeworkId 
#if(questionId)
AND question_id = :questionId
#end
ORDER BY sequence ASC
#end

#macro($getQuestion(homeworkId))
SELECT question_id FROM homework_question WHERE status = 0 AND homework_id = :homeworkId ORDER BY sequence ASC
#end

#macro($getMaxSequence(homeworkId))
SELECT MAX(sequence) FROM homework_question WHERE status = 0 AND homework_id = :homeworkId
#end

#macro($delQuestion(homeworkId,questionId))
DELETE FROM homework_question WHERE homework_id = :homeworkId
#if(questionId)
AND question_id = :questionId
#end
#end

## 获取一个作业里面的习题数量
#macro($countQuestion(homeworkId))
SELECT count(*) FROM homework_question WHERE homework_id = :homeworkId
#end

#macro($findHomeworkQuestionsByType(homeworkId,questionType))
SELECT hq.question_id FROM homework_question hq INNER JOIN question q on hq.question_id=q.id
WHERE hq.status = 0 
AND hq.homework_id = :homeworkId 
#if(questionType)
AND q.type= :questionType
#end
ORDER BY hq.sequence ASC
#end