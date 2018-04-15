## 根据假期作业专项题目id获得学生做题答案
#macro($zycQueryByItemQuestion(itemQuestionId))
SELECT t.* FROM holiday_stu_homework_item_answer t WHERE t.holiday_stu_homework_item_qid = :itemQuestionId
#end

## 根据假期题目id列表批量获得学生答案
#macro($zycQuestionByItemQuestions(itemQuestionIds))
SELECT t.* FROM holiday_stu_homework_item_answer t WHERE t.holiday_stu_homework_item_qid IN :itemQuestionIds
#end

## 根据id更新题目结果
#macro($zycUpdateResult(id,result))
UPDATE holiday_stu_homework_item_answer SET result = :result WHERE id = :id
#end
