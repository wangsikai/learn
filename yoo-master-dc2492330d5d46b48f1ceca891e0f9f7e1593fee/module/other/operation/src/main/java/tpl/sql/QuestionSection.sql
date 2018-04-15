#macro($opDelete(textbookCode,questionIds))
DELETE FROM question_section WHERE textbook_code = :textbookCode AND question_id IN (:questionIds)
#end

#macro($opFindQuestionSection1(textbookCode,questionId))
SELECT 
	DISTINCT(t2.section_code) 
FROM question_metaknow t1 
INNER JOIN metaknow_section t2 ON t1.meta_code = t2.meta_code
INNER JOIN section t3 ON t3.code = t2.section_code 
AND t3.textbook_code = :textbookCode AND t1.question_id = :questionId
#end

#macro($opFindQuestionSection2(textbookCode,questionId))
SELECT 
	DISTINCT(t2.section_code) 
FROM question_knowledge t1 
INNER JOIN knowledge_section t2 ON t1.knowledge_code = t2.knowledge_code
INNER JOIN section t3 ON t3.code = t2.section_code 
AND t3.textbook_code = :textbookCode AND t1.question_id = :questionId
#end