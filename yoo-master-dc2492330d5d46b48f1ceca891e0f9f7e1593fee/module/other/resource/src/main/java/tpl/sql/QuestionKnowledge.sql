##查询新知识点关联的题目集合
#macro($findQuestionIds(knowpointCodes,vendorId))
SELECT distinct a.question_id FROM question_knowledge a
inner join question b on a.question_id = b.id and b.vendor_id=:vendorId
WHERE a.knowledge_code in (:knowpointCodes)
#end

##根据题目获取新知识点集合
#macro($findKnowledgePointByQuestion(questionId))
SELECT k.* from question_knowledge t inner join knowledge_point k on k.code = t.knowledge_code
 where t.question_id =:questionId
#end

##根据题目获取题目知识点关联
#macro(listByQuestions(questionIds))
SELECT t.* from question_knowledge t where t.question_id in (:questionIds)
#end