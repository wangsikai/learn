##根据题目获取新知识点集合
#macro($indexFindKnowledgeReviewByQuestion(questionId))
SELECT k.code from question_knowledge_review t inner join knowledge_review k on k.code = t.knowledge_code
 where t.question_id =:questionId
#end

##根据题目获取题目知识点关联
#macro(indexListByQuestions(questionIds))
SELECT t.* from question_knowledge_review t where t.question_id in (:questionIds)
#end