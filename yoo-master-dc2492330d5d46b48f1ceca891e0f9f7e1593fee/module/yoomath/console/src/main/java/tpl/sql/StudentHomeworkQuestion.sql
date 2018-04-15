## 根据学生作业ID和习题ID获取作业习题
#macro($zycFindByStudentHomeworkIdAndQuestionId(studentHomeworkId,questionId,questionIds))
SELECT * FROM student_homework_question
WHERE student_homework_id = :studentHomeworkId
#if(questionId)
AND question_id = :questionId
#end
#if(questionIds)
AND question_id IN :questionIds
#end
#end

## 得到当前StudentHomework中还没有批改过习题
#macro($zycCountStudentHomeworkNotCorrected(stuHkId))
SELECT count(*) FROM student_homework sh, student_homework_question shq, question q
WHERE sh.id = shq.student_homework_id AND sh.status = 1 AND sh.del_status = 0
AND shq.sub_flag = 0 AND shq.auto_correct = 1 AND shq.manual_correct = 0 AND sh.id = :stuHkId
AND q.id = shq.question_id
#end

## 获取学生一次作业的订正题
#macro($zycGetCorrectQuestions(stuHkId))
SELECT question_id
FROM student_homework_question
WHERE student_homework_id = :stuHkId AND correct = 1 AND sub_flag = 0 ORDER BY id ASC
#end

## 根据学生作业ID和习题ID获取作业习题
#macro($zycQueryStuQuestions(studentHomeworkId,questionId,questionIds,newCorrect))
SELECT * FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
#if(newCorrect)
AND new_correct = 1
#end
#if(questionId)
AND question_id = :questionId
#end
#if(questionIds)
AND question_id IN :questionIds
#end
#end

## 更新学生作业题目确认状态
#macro($zycUpdateConfirmStatus(id,confirmStatus))
UPDATE student_homework_question SET confirm_status = :confirmStatus
WHERE id = :id
#end


## 批量更改学生作业题目确认状态
#macro($zycUpdateStatus(ids,status))
UPDATE student_homework_question SET confirm_status = :status
WHERE id in (:ids)
#end
