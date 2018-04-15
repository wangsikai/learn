## 统计某个教师某个教材的错题
#macro($zyStaticFallibleCount(textbookCode,teacherId))
SELECT count(*) coun,section_code FROM teacher_fallible_question 
WHERE textbook_code = :textbookCode AND teacher_id = :teacherId AND do_num > right_num AND status = 0
GROUP BY section_code
#end

## 查询
#macro($zyQuery(teacherId,leRightRate,reRightRate,leDifficulty,reDifficulty,textbookCode,sectionCodes,sectionCode,createAtDesc,updateAtDesc,difficultyDesc))
SELECT * FROM teacher_fallible_question WHERE teacher_id = :teacherId AND do_num > right_num AND status = 0
#if(leRightRate)
	AND right_rate >= :leRightRate
#end
#if(reRightRate)
	AND right_rate <= :reRightRate
#end
#if(leDifficulty)
	AND difficulty >= :leDifficulty
#end
#if(reDifficulty)
	AND difficulty <= :reDifficulty
#end
#if(textbookCode)
	AND textbook_code = :textbookCode
#end
#if(sectionCodes)
	AND section_code IN (:sectionCodes)
#end
#if(sectionCode)
	AND section_code = :sectionCode
#end
#if(timeRange == 1)
	AND date_sub(curdate(), INTERVAL 30 DAY) <= date(update_at)
#end
#if(orderCount == 1)
	ORDER BY
#end
#if(createAtDesc == 1)
	create_at DESC
#end
#if(createAtDesc == 0)
	create_at ASC
#end
#if(orderCount == 2)
	ORDER BY
#end
#if(updateAtDesc == 1)
	#if(orderCount != 2)
		,
	#end
	update_at DESC
#end
#if(updateAtDesc == 0)
	#if(orderCount != 2)
		,
	#end
	update_at ASC
#end
#if(orderCount == 3)
	ORDER BY
#end
#if(difficultyDesc == 1)
	#if(orderCount != 3)
		,
	#end
	difficulty DESC
#end
#if(difficultyDesc == 0)
	#if(orderCount != 3)
		,
	#end
	difficulty ASC
#end
#if(orderCount == 4)
	ORDER BY
#end
#if(rightRateDesc == 1)
	#if(orderCount != 4)
		,
	#end
	right_rate DESC
#end
#if(rightRateDesc == 0)
	#if(orderCount != 4)
		,
	#end
	right_rate ASC
#end
#end

## 查询 （综合知识点用）
#macro($zyQuery2(releftOpen,rerightOpen,teacherId,leRightRate,reRightRate,leDifficulty,reDifficulty,createAtDesc,updateAtDesc,difficultyDesc,metaKnowpointCodes,typeCode,typeCodes,types,leftOpen,rightOpen,sectionCodes,textBookCode,subjectCode,searchInSection))
SELECT tfq.* FROM teacher_fallible_question tfq 
#if(metaKnowpointCodes) 
INNER JOIN question_metaknow qm on qm.question_id=tfq.question_id  AND qm.meta_code IN (:metaKnowpointCodes)
#end
#if(searchInSection) 
INNER JOIN question_section qs ON qs.question_id = tfq.question_id AND qs.v2 = 1
	#if(sectionCodes)
AND qs.section_code IN (:sectionCodes)
	#end
	#if(textBookCode)
AND qs.textbook_code =:textBookCode
	#end
#end
 WHERE tfq.teacher_id = :teacherId AND tfq.do_num > tfq.right_num AND tfq.right_rate<=50 AND tfq.status = 0
#if(typeCode)
	AND tfq.type_code =:typeCode
#end
 
#if(typeCodes)
	AND tfq.type_code in (:typeCodes)
#end
#if(types)
	AND tfq.type in (:types)
#end
#if(subjectCode)
	AND tfq.subject_code =:subjectCode
#end

#if(leRightRate)
	#if(releftOpen==true)
	AND tfq.right_rate > :leRightRate
	#end
	#if(releftOpen==false)
	AND tfq.right_rate >= :leRightRate
	#end
#end
#if(reRightRate)
	#if(rerightOpen==true)
	AND tfq.right_rate < :reRightRate
	#end
	#if(rerightOpen==false)
	AND tfq.right_rate <= :reRightRate
	#end
#end
#if(leDifficulty)
	#if(leftOpen==true)
	AND tfq.difficulty > :leDifficulty
	#end
	#if(leftOpen==false)
	AND tfq.difficulty >= :leDifficulty
	#end
#end
#if(reDifficulty)
	#if(rightOpen==true)
	AND tfq.difficulty < :reDifficulty
	#end
	#if(rightOpen==false)
	AND tfq.difficulty <= :reDifficulty
	#end
#end

#if(timeRange == 1)
	AND date_sub(curdate(), INTERVAL 7 DAY) <= date(tfq.update_at)
#end

#if(timeRange == 2)
	AND date_sub(curdate(), INTERVAL 30 DAY) <= date(tfq.update_at)
#end


#if(orderCount == 1)
	ORDER BY
#end
#if(createAtDesc == 1)
	tfq.create_at DESC
#end
#if(createAtDesc == 0)
	tfq.create_at ASC
#end
#if(orderCount == 2)
	ORDER BY
#end
#if(updateAtDesc == 1)
	#if(orderCount != 2)
		,
	#end
	tfq.update_at DESC
#end
#if(updateAtDesc == 0)
	#if(orderCount != 2)
		,
	#end
	tfq.update_at ASC
#end
#if(orderCount == 3)
	ORDER BY
#end
#if(difficultyDesc == 1)
	#if(orderCount != 3)
		,
	#end
	tfq.difficulty DESC
#end
#if(difficultyDesc == 0)
	#if(orderCount != 3)
		,
	#end
	tfq.difficulty ASC
#end
#if(orderCount == 4)
	ORDER BY
#end
#if(rightRateDesc == 1)
	#if(orderCount != 4)
		,
	#end
	tfq.right_rate DESC
#end
#if(rightRateDesc == 0)
	#if(orderCount != 4)
		,
	#end
	tfq.right_rate ASC
#end
#end


##根据科目查询错题数量
#macro($zyGetKnowpointFailCount(uid,subjectCode,questionTypeCodes))
SELECT COUNT(*) count,qm.meta_code FROM question_metaknow qm INNER JOIN teacher_fallible_question tfq 
ON tfq.question_id=qm.question_id AND tfq.teacher_id =:uid AND tfq.subject_code=:subjectCode AND tfq.do_num > 
tfq.right_num AND tfq.right_rate<=50 AND tfq.status = 0
#if(questionTypeCodes)
	AND tfq.type_code NOT IN (:questionTypeCodes) 
#end
GROUP BY qm.meta_code
#end

##根据科目查询错题数量
#macro($zyGetNewKnowpointFailCount(uid,subjectCode,questionTypeCodes))
SELECT COUNT(*) count,qm.knowledge_code FROM question_knowledge qm INNER JOIN teacher_fallible_question tfq 
ON tfq.question_id=qm.question_id AND tfq.teacher_id =:uid AND tfq.subject_code=:subjectCode AND tfq.do_num > 
tfq.right_num AND tfq.right_rate<=50 AND tfq.status = 0
#if(questionTypeCodes)
	AND tfq.type_code NOT IN (:questionTypeCodes) 
#end
GROUP BY qm.knowledge_code
#end

##根据教材获取错题数量
#macro($zyGetQuestionFailCount(uid,textbookCode,subjectCode,questionTypeCodes,version))
SELECT COUNT(*) count,qs.section_code FROM question_section qs 
 INNER JOIN teacher_fallible_question tfq ON tfq.question_id=qs.question_id AND tfq.teacher_id =:uid 
 AND tfq.right_rate<=50 AND qs.textbook_code=:textbookCode AND tfq.subject_code =:subjectCode 
 AND tfq.do_num > tfq.right_num AND tfq.status = 0
#if(questionTypeCodes)
	AND tfq.type_code NOT IN (:questionTypeCodes)
#end
#if(version == 1)
	and qs.v1 = 1
#end
#if(version == 2)
	and qs.v2 = 1
#end
GROUP BY qs.section_code
#end

## 统计教材错题数量
#macro($statisTextbookFallible(textbookCodes,userId,version))
SELECT COUNT(b.question_id) cou,b.textbook_code FROM teacher_fallible_question a
INNER JOIN question_section b ON a.question_id = b.question_id 
WHERE b.textbook_code IN (:textbookCodes) AND a.teacher_id = :userId AND a.status = 0
#if(version == 1)
	and b.v1 = 1
#end
#if(version == 2)
	and b.v2 = 1
#end
GROUP BY b.textbook_code
#end

## 统计每个教材错题数量
#macro($statisEveryTextbookFallible(teacherId,categoryCode))
SELECT
	COUNT(DISTINCT(t1.question_id)) cou,
	t2.textbook_code
FROM
	teacher_fallible_question t1
INNER JOIN question_section t2 ON t1.question_id = t2.question_id AND t1.right_rate <= 50
WHERE
	t2.v2 = 1
AND t1.teacher_id = :teacherId
AND t2.textbook_code LIKE :categoryCode
AND t1.status = 0
GROUP BY
	t2.textbook_code
#end

## 查询章节数据
#macro($zyGetSectionCode(teacherId,textbookCode))
SELECT
	t2.section_code
FROM
	teacher_fallible_question t1
INNER JOIN question_section t2 ON t1.question_id = t2.question_id AND t1.right_rate <= 50
INNER JOIN section t3 on t3.code = t2.section_code and t3.name != '本章综合与测试'
WHERE
	t2.v2 = 1
AND t1.teacher_id = :teacherId
AND t2.textbook_code = :textbookCode
AND t1.status = 0
#end