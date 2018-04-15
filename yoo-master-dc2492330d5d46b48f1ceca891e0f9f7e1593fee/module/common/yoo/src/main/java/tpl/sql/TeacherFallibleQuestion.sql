## 根据题目ID和老师ID查询
#macro($find(teacherId,questionId))
SELECT * FROM teacher_fallible_question WHERE teacher_id = :teacherId AND question_id = :questionId
#end