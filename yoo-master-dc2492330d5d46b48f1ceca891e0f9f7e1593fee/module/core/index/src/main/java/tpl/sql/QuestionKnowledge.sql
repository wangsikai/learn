##根据题目获取新知识点集合
#macro($indexFindKnowledgePointByQuestion(questionId))
SELECT k.code from question_knowledge t inner join knowledge_point k on k.code = t.knowledge_code
 where t.question_id =:questionId
#end

##根据题目获取题目知识点关联
#macro(indexListByQuestions(questionIds))
SELECT t.* from question_knowledge t where t.question_id in (:questionIds)
#end


##根据题目获取知识点集合
#macro($listByQuestionForIndex(questionId))
SELECT * from question_knowledge where question_id =:questionId
#end