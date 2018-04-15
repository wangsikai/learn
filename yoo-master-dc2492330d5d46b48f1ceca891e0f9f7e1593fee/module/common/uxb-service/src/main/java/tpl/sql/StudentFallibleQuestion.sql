## 查找学生错题
#macro($findStudentFallibleQuestion(studentId,questionId))
SELECT * FROM student_fallible_question WHERE student_id = :studentId AND question_id = :questionId
#end

## 查找学生错题
#macro($queryStudentFallibleQuestion(courseId,studentId,nextUpdateAt))
SELECT * FROM student_fallible_question 
WHERE student_id = :studentId AND course_id = :courseId
#if(nextUpdateAt)
AND update_at < :nextUpdateAt
#end
ORDER BY update_at DESC
#end

## 获得questionId -> 练习次数
#macro($mgetQuestionExerciseNums(qIds,stuId))
SELECT t.question_id AS qId, t.do_num AS donum FROM student_fallible_question t
WHERE t.question_id IN :qIds AND t.student_id = :stuId
#end

## 获取错题对象
#macro($mgetFallQuestion(qIds,stuId))
SELECT * FROM student_fallible_question t
WHERE t.question_id IN :qIds AND t.student_id = :stuId and status != 2
#end