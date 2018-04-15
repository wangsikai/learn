## 获取最新一条学生作业习题的错误反馈
#macro($getByStudentHomeworkQuetionId(studentHomeorkQuestionId))
SELECT * FROM correct_question_error where student_question_id=:studentHomeorkQuestionId order by create_at DESC limit 1
#end