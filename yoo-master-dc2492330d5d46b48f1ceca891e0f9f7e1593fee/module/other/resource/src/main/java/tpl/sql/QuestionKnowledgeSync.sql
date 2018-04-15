## 通过习题ID查询同步知识点
#macro($resListKnowledgeSyncByQuestion(questionId))
SELECT t.* FROM knowledge_sync t
INNER JOIN question_knowledge_sync qks ON qks.knowledge_code=t.code
WHERE qks.question_id=:questionId
#end

## 通过习题ID查询同步知识点
#macro($resListKnowledgeSyncByQuestions(questionIds))
SELECT t.* from question_knowledge_sync t where t.question_id in (:questionIds)
#end