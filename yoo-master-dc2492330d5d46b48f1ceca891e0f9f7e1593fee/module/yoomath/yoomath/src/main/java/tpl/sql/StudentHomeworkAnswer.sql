## 查找学生答案
#macro($zyFind(homeworkId,questionId))
SELECT t1.* FROM student_homework_answer t1
INNER JOIN student_homework_question t2 ON t1.student_homework_question_id = t2.id
INNER JOIN student_homework t3 ON t2.student_homework_id = t3.id
WHERE t3.homework_id = :homeworkId AND t2.question_id = :questionId AND t3.status = 1
ORDER BY t1.student_homework_question_id ASC,t1.sequence ASC
#end

## 判断一个学生是否做作业了
#macro($zyIsDoHomework(stuHkId))
SELECT count(t1.id) 
FROM student_homework_answer t1 LEFT JOIN student_homework_question t2 ON t1.student_homework_question_id = t2.id 
WHERE t2.student_homework_id = :stuHkId AND t1.content IS NOT NULL AND t1.content != ''
#end

## 查找某份作业下面的某道题的所有学生作业答案列表
#macro($zyFindByQuestionId(homeworkId,questionId))
SELECT t1.* FROM student_homework_answer t1
INNER JOIN student_homework_question t2 ON t1.student_homework_question_id = t2.id
INNER JOIN student_homework t3 ON t2.student_homework_id = t3.id
WHERE t3.homework_id = :homeworkId AND t2.question_id = :questionId
ORDER BY t1.student_homework_question_id ASC,t1.sequence ASC
#end

## 根据学生作业题目id查找答案
#macro($zyFindByStuHkQuestion(stuHkQuestionId))
SELECT t.* FROM student_homework_answer t WHERE t.student_homework_question_id = :stuHkQuestionId
#end