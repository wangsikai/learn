
## 根据习题查询
#macro($listByQuestion(questionId))
SELECT t.* from question_metaknow t where t.question_id =:questionId
#end

## 根据习题查询
#macro($listByQuestions(questionIds))
SELECT t.* from question_metaknow t where t.question_id in (:questionIds)
#end

## 根据学科获得元知识点对应的没有新知识点习题个数
#macro($listQuestionCountsByNoNewKnowledge(subjectCode))
SELECT t.`meta_code` AS code,COUNT(t.`question_id`) as counts FROM `question_metaknow` t
 INNER JOIN `meta_knowpoint` mk ON mk.code=t.`meta_code` AND mk.subject_code=:subjectCode
 INNER JOIN question q ON q.id = t.`question_id`
 WHERE q.knowledge_create_id IS NULL
 GROUP BY t.`meta_code`
#end