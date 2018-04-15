##根据题目id查询对应知识点集合
#macro($queryKnowledgeByQuestionId(questionId))
SELECT knowledge_code FROM question_knowledge WHERE question_id =:questionId
#end

## 查询知识点下的题目数量
#macro($queryKnowledgeQuestionCount(subjectCode))
SELECT t.knowledge_code, count(*) AS c FROM question_knowledge t
INNER JOIN knowledge_point k ON t.knowledge_code = k.code
INNER JOIN question q ON t.question_id = q.id
WHERE k.subject_code = :subjectCode AND q.status = 2 AND q.del_status = 0 GROUP BY t.knowledge_code HAVING c > 0
#end

##批量查询对应知识点集合
#macro($mgetByQuestions(questionIds))
SELECT t.* from question_knowledge t where t.question_id in (:questionIds)
#end