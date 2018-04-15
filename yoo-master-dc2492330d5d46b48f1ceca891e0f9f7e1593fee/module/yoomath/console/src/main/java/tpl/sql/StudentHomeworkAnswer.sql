## 根据学生作业习题ID获取学生作业答案
#macro($zycFindStudentHomeworkAnswer(studentHomeworkQuestionId,studentHomeworkQuestionIds))
SELECT * FROM student_homework_answer WHERE 1=1
#if(studentHomeworkQuestionId)
AND student_homework_question_id = :studentHomeworkQuestionId
#end
#if(studentHomeworkQuestionIds)
AND student_homework_question_id IN :studentHomeworkQuestionIds
#end
ORDER BY sequence ASC
#end

##统计还没有批改的题目数量
#macro($zycCountNotCorrected(studentHomeworkIds))
SELECT COUNT(*) cou,c.id FROM
student_homework_answer a
INNER JOIN student_homework_question b ON a.student_homework_question_id = b.id
INNER JOIN student_homework c ON b.student_homework_id = c.id
WHERE c.id IN :studentHomeworkIds AND a.result = 0 GROUP BY c.id
#end