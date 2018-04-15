## 根据专项题目id来查询答题图片
#macro($findByItemQuestion(id))
SELECT * FROM holiday_stu_homework_answer_image t WHERE t.holiday_stu_item_question_id = :id
#end

## 根据多个专项题目id来查询答题图片
#macro($findByItemQuestions(ids))
SELECT * FROM holiday_stu_homework_answer_image t WHERE t.holiday_stu_item_question_id IN :ids
#end

## 根据holiday_stu_item_question_id删除数据
#macro($deleteByHolidayStuHkQuestion(id))
DELETE FROM holiday_stu_homework_answer_image WHERE holiday_stu_item_question_id = :id
#end