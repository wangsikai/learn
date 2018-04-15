## 根据多个StudentHomeworkQuestion查找图片答题数据
#macro($mgetByStuHkQuestion(stuHkQuestionIds))
SELECT t.* FROM student_homework_answer_image t
WHERE t.student_homework_question_id IN :stuHkQuestionIds
#end

## 根据一个学生StudentHomeworkQuestion查找答题图片数据
#macro($findByStuHkQuestion(stuHkQuestionId))
SELECT t.* FROM student_homework_answer_image t
WHERE t.student_homework_question_id = :stuHkQuestionId
#end

## 根据StudentHomeworkQuestion删除数据
#macro($deleteByStuHkQuestion(id))
DELETE FROM student_homework_answer_image WHERE student_homework_question_id = :id
#end