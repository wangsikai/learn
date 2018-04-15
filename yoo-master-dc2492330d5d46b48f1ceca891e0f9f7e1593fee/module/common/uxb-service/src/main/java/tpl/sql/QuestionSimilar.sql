## 获取相似题
#macro($querySimilarQuestionByBaseId(baseQuestionId))
SELECT * FROM question_similar where status = 0 and change_flag = 1 and base_question_id=:baseQuestionId
#end

## 批量获取相似题
#macro($querySimilarQuestionByBaseIds(baseQuestionIds))
SELECT * FROM question_similar where status = 0 and change_flag = 1 and base_question_id in (:baseQuestionIds)
#end