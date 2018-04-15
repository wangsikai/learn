## 根据习题查询
#macro($listByQuestionForIndex(questionId))
SELECT t.* from question_metaknow t where t.question_id =:questionId
#end

## 根据习题查询
#macro($listByQuestionsForIndex(questionIds))
SELECT t.* from question_metaknow t where t.question_id in (:questionIds)
#end