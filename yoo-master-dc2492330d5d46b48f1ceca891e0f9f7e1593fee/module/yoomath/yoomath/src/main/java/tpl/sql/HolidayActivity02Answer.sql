## 根据questionsId查询pk答案
#macro($getAnswer(questionsId))
SELECT * FROM holiday_activity_02_answer WHERE questions_id = :questionsId
#end


