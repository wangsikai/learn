## 删除答案
#macro($deleteByQuestion(questionId))
DELETE FROM answer where question_id =:questionId
#end