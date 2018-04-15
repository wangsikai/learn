## 通过习题ID查询同步知识点
#macro($resListKnowledgeReviewByQuestion(questionId))
SELECT t.* FROM knowledge_review t
INNER JOIN question_knowledge_review qks ON qks.knowledge_code=t.code
WHERE qks.question_id=:questionId
#end

## 通过习题ID查询同步知识点
#macro($resListKnowledgeReviewByQuestions(questionIds))
SELECT t.* from question_knowledge_review t where t.question_id in (:questionIds)
#end