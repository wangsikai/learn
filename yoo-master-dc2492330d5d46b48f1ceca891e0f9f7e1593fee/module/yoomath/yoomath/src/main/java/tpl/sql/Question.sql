## 按条件从数据库里面随机获取一些题目
#macro($zyPullQuestions(sectionCode,types,exQIds,count,minDifficulty,maxDifficulty))
SELECT * FROM question WHERE sub_flag = 0
#if(sectionCode)
	AND section_code = :sectionCode
#end
#if(types)
	AND type IN (:types)
#end
#if(exQIds)
	AND id NOT IN (:exQIds)
#end
#if(minDifficulty)
	AND difficulty > :minDifficulty
#end
#if(maxDifficulty)
	AND difficulty < :maxDifficulty
#end
ORDER BY RAND() LIMIT :count
#end

## 按照题目篮子要求获取题目列表
#macro($previewQuestions(ids))
SELECT * FROM question t WHERE t.sub_flag = 0
AND t.id in :ids ORDER BY t.type ASC, t.difficulty DESC
#end

##判断是否有简答题
#macro($hasQuestionAnswering(ids))
SELECT count(id) FROM question WHERE id IN (:ids) AND type = 5
#end

## 查找需要批改的题目(Homework未过批改时间)
#macro($queryNeedCorrectQuestion(hkId))
SELECT q.* FROM question q WHERE q.id IN (
   SELECT shq.question_id FROM student_homework_question shq
   INNER JOIN student_homework sh ON shq.student_homework_id = sh.id
   WHERE sh.homework_id = :hkId AND sh.status = 1 AND shq.correct_type=3
   )
GROUP BY q.id;
#end

## 查找需要批改的题目(Homework已经过了批改时间)
#macro($queryNeedCorrectQuestionAll(hkId))
SELECT q.* FROM question q WHERE q.id IN (
   SELECT shq.question_id FROM student_homework_question shq
   INNER JOIN student_homework sh ON shq.student_homework_id = sh.id
   WHERE sh.homework_id = :hkId AND sh.status = 1 AND shq.correct_type=3
   )
GROUP BY q.id;
#end

## 查询需要批改的题目ID列表(未过人工批改时间)
#macro($queryNeedCorrectQuestions(hkId))
SELECT DISTINCT(shq.question_id) 
FROM student_homework_question shq
INNER JOIN student_homework sh ON shq.student_homework_id = sh.id
INNER JOIN homework h ON h.id = sh.homework_id
WHERE 
	h.id = :hkId 
AND sh.status = 1 
AND shq.correct_type=3
#end

## 查找需要批改的题目ID列表(已过人工批改时间)
#macro($queryNeedCorrectQuestionsAll(hkId))
SELECT DISTINCT(shq.question_id) 
FROM student_homework_question shq
INNER JOIN student_homework sh ON shq.student_homework_id = sh.id
WHERE 
	sh.homework_id = :hkId 
AND sh.status = 1 
AND shq.correct_type=3
#end

## Recommend-获取教辅习题
#macro($listRecommendQuestionFromUserBook(teacherId, sectionCode, sectionCodes, eliminateQuestionIds,questionType,diff1,diff2,kpCode))
SELECT DISTINCT(bq.question_id) as question_id,t.book_id as book_id FROM user_school_book t
INNER JOIN book_version bv ON bv.book_id=t.book_id AND bv.main_flag=1 AND bv.status=2
INNER JOIN book_catalog bc ON bc.book_version_id=bv.id
INNER JOIN book_catalog_section bcs ON bcs.book_catalog_id=bc.id
#if(sectionCode)
AND bcs.section_code=:sectionCode
#end
#if(sectionCodes)
AND bcs.section_code in (:sectionCodes)
#end
INNER JOIN book_question bq ON bq.book_version_id=bv.id AND bq.book_catalog_id=bc.id
INNER JOIN question q on q.id=bq.question_id AND q.status=2 AND q.del_status=0 
#if(questionType)
 	AND q.type IN (1,3,5)
#elseif
 	AND q.type IN (1,3)
#end
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
#if(kpCode)
	inner join question_knowledge kk on kk.question_id = q.id and kk.knowledge_code = :kpCode
#end
#if(eliminateQuestionIds)
 AND bq.question_id not in (:eliminateQuestionIds)
#end
WHERE t.user_id=:teacherId and t.status=0
#end

## Recommend-获取热门习题
## 添加教辅书本的限定条件
#macro($listRecommendQuestionFromHot(sectionCode, sectionCodes, num, eliminateQuestionIds,questionType,diff1,diff2,kpCode))
SELECT DISTINCT(hq.question_id) FROM homework_question hq 
INNER JOIN question q on q.id=hq.question_id AND q.status=2 AND q.del_status=0 
#if(questionType)
 	AND q.type IN (1,3,5)
#elseif
 	AND q.type IN (1,3)
#end
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
#if(kpCode)
	inner join question_knowledge kk on kk.question_id = q.id and kk.knowledge_code = :kpCode
#end
INNER JOIN book_question bq ON bq.question_id=q.id
INNER JOIN question_section qs ON qs.question_id=hq.question_id and qs.v2=1
#if(sectionCode)
AND qs.section_code=:sectionCode
#end
#if(sectionCodes)
AND qs.section_code in (:sectionCodes)
#end
#if(eliminateQuestionIds)
 WHERE hq.question_id not in (:eliminateQuestionIds)
#end
ORDER BY RAND() DESC LIMIT :num
#end

## Recommend-获取错题
#macro($listRecommendQuestionFromFall(teacherId, sectionCode, sectionCodes, num, eliminateQuestionIds,questionType,diff1,diff2,kpCode))
SELECT DISTINCT(t.question_id) FROM teacher_fallible_question t
INNER JOIN question q ON q.id=t.question_id AND q.status=2 AND q.del_status=0 
#if(questionType)
 	AND q.type IN (1,3,5)
#elseif
 	AND q.type IN (1,3)
#end
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
#if(kpCode)
	inner join question_knowledge kk on kk.question_id = q.id and kk.knowledge_code = :kpCode
#end
INNER JOIN question_section qs ON qs.question_id=q.id AND qs.v2=1
#if(sectionCode)
AND qs.section_code=:sectionCode
#end
#if(sectionCodes)
AND qs.section_code in (:sectionCodes)
#end
WHERE t.teacher_id=:teacherId AND t.right_rate <= 50
#if(eliminateQuestionIds)
 AND t.question_id not in (:eliminateQuestionIds)
#end
ORDER BY RAND() LIMIT :num
#end

## Recommend-获取好题
#macro($listRecommendQuestionFromGood(teacherId, sectionCode, sectionCodes, num, eliminateQuestionIds,questionType,diff1,diff2,kpCode))
SELECT DISTINCT(t.question_id) FROM question_collection t
INNER JOIN question q ON q.id=t.question_id AND q.status=2 AND q.del_status=0 
#if(questionType)
 	AND q.type IN (1,3,5)
#elseif
 	AND q.type IN (1,3)
#end
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
#if(kpCode)
	inner join question_knowledge kk on kk.question_id = q.id and kk.knowledge_code = :kpCode
#end
INNER JOIN question_section qs ON qs.question_id=t.question_id and qs.v2=1
#if(sectionCode)
 AND qs.section_code=:sectionCode
#end
#if(sectionCodes)
 AND qs.section_code in (:sectionCodes)
#end
WHERE t.user_id=:teacherId
#if(eliminateQuestionIds)
 AND t.question_id not in (:eliminateQuestionIds)
#end
ORDER BY RAND() LIMIT :num
#end

## Recommend-获取班级薄弱知识点习题
#macro($listRecommendQuestionFromClassWeek(teacherId, sectionCode, sectionCodes, num, eliminateQuestionIds,questionType,diff1,diff2,kpCode))
SELECT 
	DISTINCT(a.id) AS question_id
FROM 
	(
		SELECT 
			q.* 
		FROM 
			question_knowledge qk 
			INNER JOIN question q 
				ON q.id=qk.question_id 
				AND qk.knowledge_code IN(
					SELECT 
						kp.code 
					FROM knowledge_point kp 
					INNER JOIN knowledge_section ks 
					ON kp.code = ks.knowledge_code 
					WHERE 1=1
					#if(sectionCode)
					AND ks.section_code = :sectionCode
					#end
					#if(sectionCodes)
					AND ks.section_code IN (:sectionCodes) 
					#end
					AND kp.status=0 AND kp.difficulty IN(1,2) AND kp.code IN(
						SELECT 
							dckp.knowpoint_code 
						FROM diagno_class_kp dckp 
						INNER JOIN homework_class c 
						ON c.id=dckp.class_id 
						AND c.teacher_id = :teacherId AND c.status=0 AND (dckp.right_count+1)/(dckp.do_count+2) <=0.3
					)
				)
				#if(questionType)
					and q.type = :questionType
				#end
				#if(diff1)
					and q.difficulty >= :diff1
				#end
				#if(diff2)
					and q.difficulty < :diff2
				#end
				#if(kpCode)
					inner join question_knowledge kk on kk.question_id = q.id and kk.knowledge_code = :kpCode
				#end
		WHERE 
			q.status=2 
		AND q.del_status=0 
		#if(questionType)
 			AND q.type IN (1,3,5)
		#elseif
 			AND q.type IN (1,3)
		#end
		#if(eliminateQuestionIds)
		AND qk.question_id NOT in (:eliminateQuestionIds)
		#end
	) a ORDER BY a.difficulty DESC LIMIT :num
#end

## Recommend-根据章节获取对应的考点
#macro($listExampointForRecommendQuestion(sectionCode, sectionCodes))
SELECT ep.* FROM examination_point ep
INNER JOIN examination_point_knowledge_point ek ON ek.examination_point_id=ep.id
INNER JOIN knowledge_point kp ON kp.code=ek.knowledge_point_code AND kp.status=0 AND (kp.difficulty=1 OR kp.difficulty=2)
INNER JOIN knowledge_section ks ON ks.knowledge_code=kp.code
#if(sectionCode)
AND ks.section_code=:sectionCode
#end
#if(sectionCodes)
AND ks.section_code in (:sectionCodes)
#end
WHERE ep.status=0 AND (ep.frequency=0 OR ep.frequency=2)
#end

## Recommend-根据考点获取对应的习题
#macro($listRecommendQuestionFromExampoint(questionIds, num,questionType,diff1,diff2,kpCode))
select q.* from question q
#if(kpCode)
	inner join question_knowledge kk on kk.question_id = q.id and kk.knowledge_code = :kpCode
#end
INNER JOIN book_question bq ON bq.question_id=q.id
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
where q.status=2 AND q.del_status=0 
#if(questionType)
 	AND q.type IN (1,3,5)
#elseif
 	AND q.type IN (1,3)
#end
and q.id in (:questionIds)
ORDER BY RAND() LIMIT :num
#end

## Recommend-直接从章节对应关系获取习题
## 添加教辅书的
#macro($listRecommendQuestionFromQuestionSection(sectionCode, sectionCodes, num, eliminateQuestionIds, hasQuestionIds,questionType,diff1,diff2,kpCode))
SELECT DISTINCT(q.id) FROM question q
INNER JOIN book_question bq ON bq.question_id=q.id
INNER JOIN question_section qs ON qs.question_id=q.id AND qs.v2=1
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
#if(kpCode)
	inner join question_knowledge kk on kk.question_id = q.id and kk.knowledge_code = :kpCode
#end
#if(sectionCode)
AND qs.section_code=:sectionCode
#end
#if(sectionCodes)
AND qs.section_code in (:sectionCodes)
#end
WHERE q.status=2 AND q.del_status=0 
#if(questionType)
 	AND q.type IN (1,3,5)
#elseif
 	AND q.type IN (1,3)
#end
#if(eliminateQuestionIds)
 AND q.id not in (:eliminateQuestionIds)
#end
#if(hasQuestionIds)
 AND (q.same_show_id is null or q.same_show_id not in (:hasQuestionIds))
#end
ORDER BY RAND() LIMIT :num
#end