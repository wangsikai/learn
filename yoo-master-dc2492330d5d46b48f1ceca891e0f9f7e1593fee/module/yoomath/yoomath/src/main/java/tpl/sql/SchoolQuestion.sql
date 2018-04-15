## 知识点对应学校题目数量
#macro($statisKnowPointSchool(subjectCode,schoolId,qtCodes))
  SELECT COUNT(a.question_id) cou,a.meta_code FROM question_metaknow a
  INNER JOIN school_question b ON a.question_id = b.question_id AND b.subject_code =:subjectCode and school_id=:schoolId 
  #if(qtCodes)
  	and b.type_code not in (:qtCodes)
  #end
  GROUP BY a.meta_code
#end

##获取单个学校题库
#macro($getQuestionSchool(questionId,schoolId))
  select * from school_question where school_id=:schoolId and question_id=:questionId
#end

##获取多个收藏
#macro($getQuestionSchools(questionIds,schoolId))
  select * from school_question where school_id=:schoolId and question_id in :questionIds
#end

## 章节码对应学校题目数量
#macro($statisSectionSchool(textbookCode,schoolId,qtCodes,version))
	SELECT COUNT(b.question_id) cou,b.section_code FROM school_question a 
	INNER JOIN question q  ON a.question_id = q.id and q.scene_code !=1
	INNER JOIN question_section b ON a.question_id = b.question_id and b.textbook_code =:textbookCode and a.school_id=:schoolId 
	#if(qtCodes)
		and a.type_code not in(:qtCodes)
	#end
	#if(version == 1)
		and b.v1 = 1
	#end
	#if(version == 2)
		and b.v2 = 1
	#end
	GROUP BY b.section_code
#end


## 统计教材学校数量
#macro($statisTextbookSchool(textbookCodes,schoolId,qtCodes))
SELECT COUNT(b.question_id) cou,b.textbook_code 
FROM school_question a INNER JOIN question_section b ON a.question_id = b.question_id 
WHERE b.textbook_code IN (:textbookCodes) AND a.school_id = :schoolId 
#if(qtCodes)
	and a.type_code not in (:qtCodes)
#end
GROUP BY b.textbook_code
#end

## 统计学校题目数量
#macro($countBySchool(schoolId))
SELECT count(*) FROM school_question t WHERE t.school_id = :schoolId
#end

## 新知识点对应学校题目数量
#macro($getNewKnowpointSchoolQCount(subjectCode,schoolId,qtCodes))
  SELECT COUNT(a.question_id) count,a.knowledge_code FROM question_knowledge a
  INNER JOIN question qs  ON a.question_id = qs.id and qs.scene_code !=1
  INNER JOIN school_question b ON a.question_id = b.question_id AND b.subject_code =:subjectCode and b.school_id=:schoolId 
  #if(qtCodes)
  	and b.type_code not in (:qtCodes)
  #end
  GROUP BY a.knowledge_code
#end