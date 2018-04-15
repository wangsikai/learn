## 按照题目id查找
#macro($findByQuestionId(version,questionId))
	SELECT * FROM question_section WHERE question_id = :questionId
	#if(version == 1)
		AND v1 = 1
	#end
	#if(version == 2)
		AND v2 = 1
	#end
#end

## 章节码对应题目数量(只统计已通过校验、非校本的题目)
#macro($statisSectionQuestionCount(version,textbookCode,excludeQuestionTypeCodes))
	SELECT 
		COUNT(b.question_id) cou,b.section_code 
	FROM 
		question a INNER JOIN question_section b ON a.id = b.question_id 
		AND b.textbook_code =:textbookCode AND a.status = 2 AND a.school_id = 0 
	#if(version == 1)
		AND v1 = 1
	#end
	#if(version == 1)
		AND v2 = 1
	#end
	#if(excludeQuestionTypeCodes)
		AND a.type_code NOT IN(:excludeQuestionTypeCodes)
	#end
	GROUP BY b.section_code
#end