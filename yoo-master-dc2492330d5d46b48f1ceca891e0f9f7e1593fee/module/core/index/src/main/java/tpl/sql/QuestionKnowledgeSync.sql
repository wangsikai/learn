##根据题目获取新知识点集合
#macro($indexFindKnowledgeSyncByQuestion(questionId))
SELECT k.code from question_knowledge_sync t inner join knowledge_sync k on k.code = t.knowledge_code
 where t.question_id =:questionId
#end

##根据题目获取题目知识点关联
#macro(indexListByQuestions(questionIds))
SELECT t.* from question_knowledge_sync t where t.question_id in (:questionIds)
#end