## 根据学生作业习题ID获取学生作业答案
#macro($findStudentHomeworkAnswer(studentHomeworkQuestionId,studentHomeworkQuestionIds))
SELECT * FROM student_homework_answer WHERE 1=1
#if(studentHomeworkQuestionId)
AND student_homework_question_id = :studentHomeworkQuestionId
#end
#if(studentHomeworkQuestionIds)
AND student_homework_question_id IN :studentHomeworkQuestionIds
#end
ORDER BY sequence ASC
#end


## 获取未批改的答案数量
#macro($countNotCorrect(homeworkId))
SELECT COUNT(*) FROM 
student_homework_answer a 
INNER JOIN student_homework_question b ON a.student_homework_question_id = b.id
INNER JOIN student_homework c ON b.student_homework_id = c.id
INNER JOIN homework d ON c.homework_id = d.id
WHERE d.id = :homeworkId AND a.result = 0
#end


## 统计学生作业的未批改数量
#macro($countNotCorrected(studentHomeworkIds))
SELECT COUNT(*) cou,c.id FROM 
student_homework_answer a 
INNER JOIN student_homework_question b ON a.student_homework_question_id = b.id
INNER JOIN student_homework c ON b.student_homework_id = c.id
WHERE c.id IN :studentHomeworkIds AND a.result = 0 GROUP BY c.id
#end

## 更新用户作业答案
#macro($updateUserAnswer(shqId,sequence,answerAt,content,contentAscii,answerId,sequence))
UPDATE student_homework_answer SET
sequence = :sequence,
answer_at = :answerAt,
#if(content)
content = :content,
#end
#if(contentAscii)
content_ascii = :contentAscii,
#end
answer_id = :answerId
WHERE student_homework_question_id = :shqId AND sequence = :sequence
#end

## 删除用户作业答案
#macro($deleteUserAnswer(shqId))
DELETE FROM student_homework_answer WHERE student_homework_question_id = :shqId
#end

## 批改学生作业题目
#macro($zyCorrect(stuHkQId,result,correctAt))
UPDATE student_homework_answer SET result = :result 
#if(correctAt)
,correct_at=:correctAt
#end
WHERE student_homework_question_id = :stuHkQId
#end

## 根据答案的id更新批改结果
#macro($zyCorrectById(id,result,correctAt))
UPDATE student_homework_answer SET result = :result 
#if(correctAt)
,correct_at=:correctAt
#end
 WHERE id = :id
#end