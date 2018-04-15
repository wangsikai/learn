## 统计某个学生某个教材的错题
#macro($zyStaticFallibleCount(textbookCode,studentId,version))
SELECT 
	count(t1.id) coun,t2.section_code 
FROM student_fallible_question t1 INNER JOIN question_section t2 ON t1.question_id = t2.question_id
WHERE t2.textbook_code = :textbookCode AND t1.student_id = :studentId AND t1.status = 0 AND t1.mistake_num > 0
#if(version == 1)
	AND v1 = 1
#end
#if(version == 2)
	AND v2 = 1
#end
AND type not in(2,4,6)
GROUP BY t2.section_code
#end

## 统计某个学生多个教材的错题
#macro($zyStaticFallibleCounts(textbookCodes,studentId))
SELECT 
	count(t1.id) coun,t2.section_code 
FROM student_fallible_question t1 INNER JOIN question_section t2 ON t1.question_id = t2.question_id
WHERE t2.textbook_code in (:textbookCodes) AND t1.student_id = :studentId AND t1.status = 0 AND t1.mistake_num > 0
AND t1.type not in(2,4,6)
GROUP BY t2.section_code
#end

## 查询
#macro($zyQuery(studentId,textbookCode,sectionCodes,sectionCode,updateAtDesc,createAtDesc,mistakeNumDesc))
SELECT
	t1.*
FROM student_fallible_question t1 INNER JOIN question_section t2 ON t1.question_id = t2.question_id
WHERE t1.student_id = :studentId AND t1.status = 0 AND t1.mistake_num > 0
#if(textbookCode)
 AND t2.textbook_code = :textbookCode
#end
#if(sectionCodes)
 AND t2.section_code IN (:sectionCodes)
#end
#if(sectionCode)
 AND t2.section_code = :sectionCode
#end
GROUP BY t1.id
#if(updateAtDesc == 1)
 ORDER BY t1.update_at DESC
#end
#if(updateAtDesc == 0)
 ORDER BY t1.update_at ASC
#end
#if(createAtDesc == 1)
 ORDER BY t1.create_at DESC
#end
#if(createAtDesc == 0)
 ORDER BY t1.create_at ASC
#end
#if(mistakeNumDesc == 1)
 ORDER BY t1.mistake_num DESC
#end
#if(mistakeNumDesc == 0)
 ORDER BY t1.mistake_num ASC
#end
#end

## 查询
#macro($zyQueryByCursor(studentId,textbookCode,updateAt,createAt,sectionCodes))
SELECT t1.* FROM student_fallible_question t1
INNER JOIN question_section t2 ON t1.question_id = t2.question_id
WHERE t1.student_id = :studentId AND t1.status = 0 AND t1.mistake_num > 0
#if(textbookCode)
 AND t2.textbook_code = :textbookCode
#end
#if(sectionCodes)
 AND t2.section_code in (:sectionCodes)
#end
#if(updateAt)
 AND t1.update_at < :updateAt
#end
#if(createAt)
 AND t1.create_at < :createAt
#end
#if(updateAt)
ORDER BY t1.update_at DESC
#end
#if(createAt)
ORDER BY t1.create_at DESC
#end
#end

## 根据题目ID和学生ID查询
#macro($zyFind(studentId,questionId))
SELECT * FROM student_fallible_question WHERE student_id = :studentId AND question_id = :questionId AND status = 0
#end

## 统计学生练习题目次数
#macro($zyCountDoNum(studentId))
SELECT sum(do_num) FROM student_fallible_question WHERE student_id = :studentId
#end

## 统计学生教材章节下的错题数量
#macro($zyCountTS(studentId,categoryCode))
SELECT count(1) AS amount, qs.textbook_code, qs.section_code FROM student_fallible_question t
INNER JOIN question_section qs ON qs.question_id = t.question_id AND qs.v2 = 1
WHERE t.student_id = :studentId AND t.status = 0 AND t.mistake_num > 0 AND qs.textbook_code LIKE :categoryCode AND (t.type NOT IN (2, 4, 6) OR t.type IS NULL)
GROUP BY qs.textbook_code, qs.section_code
ORDER BY qs.textbook_code ASC
#end

## 统计一段时间内的错题数量
#macro($zyCountByDate(beginDate,endDate,studentId,categoryCode))
select count(*) from (
SELECT t.* FROM student_fallible_question t LEFT JOIN question_section qs ON qs.question_id = t.question_id AND qs.v2 = 1
WHERE t.student_id = :studentId AND t.status = 0 AND t.mistake_num > 0 AND (t.type NOT IN (2, 4, 6) OR t.type IS NULL)
#if(beginDate)
 AND t.update_at >= :beginDate
#end
#if(endDate)
 AND t.update_at <= :endDate
#end
GROUP BY t.id
) z
#end

## 统计学生其他错题数量
#macro($zyCountOther(studentId,categoryCode))
select 
(
SELECT 
	COUNT(DISTINCT(t.question_id)) 
FROM 
	student_fallible_question t 
LEFT JOIN question_section qs ON qs.question_id = t.question_id 
AND qs.v2 = 1
WHERE t.status = 0 
AND t.mistake_num > 0 
AND t.type NOT IN (2, 4, 6) 
AND t.question_id is not null
AND t.student_id = :studentId 
) - (
SELECT 
	COUNT(DISTINCT(t.id)) 
FROM 
	student_fallible_question t 
LEFT JOIN question_section qs ON qs.question_id = t.question_id 
AND qs.v2 = 1
WHERE t.status = 0 
AND t.mistake_num > 0 
AND t.type NOT IN (2, 4, 6) 
AND t.student_id = :studentId 
AND qs.textbook_code LIKE :categoryCode
)
#end

## 统计OCR识别的题目数量
#macro($zyCountOCR(studentId,categoryCode))
SELECT count(*) FROM student_fallible_question t WHERE t.student_id = :studentId AND t.status = 0
AND t.mistake_num > 0 AND t.ocr_image_id > 0 AND t.question_id IS NULL
#end

## 查询(新 手机端)
#macro($zyQueryStuFall(studentId,sectionCodes,createAtCursor,categoryCode,other,ocr,dateParam))
SELECT t.* FROM student_fallible_question t LEFT JOIN question_section qs ON qs.question_id = t.question_id AND qs.v2 = 1
WHERE t.status = 0 AND t.student_id = :studentId AND t.mistake_num > 0 AND (t.type NOT IN (2, 4, 6) OR t.type IS NULL)
#if(sectionCodes)
 AND qs.section_code IN :sectionCodes
#end
#if(createAtCursor)
 AND t.update_at >= :createAtCursor
#end
#if(other == 1)
  AND qs.textbook_code NOT LIKE :categoryCode
#end
#if(ocr == 1)
  AND t.ocr_image_id > 0 AND t.question_id IS NULL
#end
AND t.update_at < :dateParam
GROUP BY t.id
ORDER BY t.update_at DESC
#end

## 查询错题错误人数
#macro($zyCountMistakePeople(questionIds))
SELECT count(1) amount, question_id FROM student_fallible_question WHERE question_id IN :questionIds GROUP BY question_id
#end

## 查询学生错题
#macro($zyFindByQuestion(studentId,questionIds))
SELECT * FROM student_fallible_question WHERE student_id = :studentId AND status = 0 AND mistake_num > 0 AND question_id IN :questionIds
#end

## 知识点对应题目学生错题的数量
#macro($statisKnowPoint(userId))
  SELECT COUNT(a.question_id) cou,a.meta_code FROM question_metaknow a
  INNER JOIN student_fallible_question b ON a.question_id = b.question_id and student_id=:userId
  and b.status=0 and b.mistake_num > 0 and b.type not in(2,4,6)
  GROUP BY a.meta_code
#end

## 新知识点对应题目学生错题的数量
#macro($statisNewKnowPoint(userId))
  SELECT COUNT(a.question_id) cou,a.knowledge_code FROM question_knowledge a
  INNER JOIN student_fallible_question b ON a.question_id = b.question_id and student_id=:userId
  and b.status=0 and b.mistake_num > 0 and b.type not in(2,4,6)
  GROUP BY a.knowledge_code
#end

## 学生错题导出-统计错题数量（章内去重）
#macro($zyStuExportCount(studentId, sectionCodes, timeScope, questionTypes, errorTimes))
SELECT COUNT(a.id) FROM (
 SELECT COUNT(t.id) as id FROM student_fallible_question t
 INNER JOIN question q ON q.id = t.question_id
 #if(questionTypes)
 AND q.type in (:questionTypes)
 #end
 INNER JOIN question_section t2 ON t.question_id = t2.question_id AND SUBSTRING(t2.section_code, 1, 10) IN (:sectionCodes)
 WHERE t.student_id=:studentId AND t.status = 0 AND t.mistake_num > 0
 #if(timeScope)
 AND t.update_at > :timeScope
 #end
 #if(errorTimes == 1 || errorTimes == 2)
 AND t.mistake_times =:errorTimes
 #end
 #if(errorTimes == 3)
 AND t.mistake_times >= 3
 #end
 GROUP BY SUBSTRING(t2.section_code, 1, 10), t.question_id
) a
#end

## 学生错题导出-错题（章内去重）
#macro($zyStuExportFQuestions(studentId, sectionCodes, timeScope, questionTypes, errorTimes))
SELECT t.id as id,t.question_id as qid,t2.section_code as scode,t2.textbook_code as tcod FROM student_fallible_question t
 INNER JOIN question_section t2 ON t.question_id = t2.question_id AND SUBSTRING(t2.section_code, 1, 10) IN (:sectionCodes)
 INNER JOIN question q ON q.id = t.question_id
 #if(questionTypes)
 AND q.type in (:questionTypes)
 #end
 WHERE t.student_id=:studentId AND t.status = 0 AND t.mistake_num > 0
 #if(timeScope)
 AND t.update_at > :timeScope
 #end
 #if(errorTimes == 1 || errorTimes == 2)
 AND t.mistake_times =:errorTimes
 #end
 #if(errorTimes == 3)
 AND t.mistake_times >= 3
 #end
 GROUP BY SUBSTRING(t2.section_code, 1, 10),t.question_id
 ORDER BY t.create_at ASC
#end

## 判断学生指定教材中是否包含错题
#macro($zyStuTextbookFQuestionCount(studentId, textbookCodes))
SELECT COUNT(t.id) FROM student_fallible_question t
INNER JOIN question_section s ON s.question_id=t.question_id AND s.textbook_code IN (:textbookCodes)
WHERE t.student_id=:studentId AND t.status = 0 AND t.mistake_num > 0
#end

## 获取学生第一次错题对象
#macro($getFirst(studentId))
	SELECT * FROM student_fallible_question WHERE student_id =:studentId and mistake_num > 0 and status = 0 ORDER BY create_at ASC LIMIT 0,1
#end

## 获取学生错题次数
#macro($getFallibleCountByStuId(studentId))
	SELECT count(id) FROM student_fallible_question WHERE student_id =:studentId and mistake_num > 0 and status = 0 AND (TYPE NOT IN(2,4,6) OR TYPE IS NULL)
#end

## 统计某个学生多个教材下第一级章下的不重复错题数量（学生导出错题使用）
#macro($zyStuFallibleLevel1SectionCounts(textbookCodes,studentId))
SELECT COUNT(a.coun) as cont,a.sec1 as scode FROM (
SELECT COUNT(t1.id) coun,t1.question_id,SUBSTRING(t2.section_code, 1, 10) AS sec1
FROM student_fallible_question t1 
INNER JOIN question q on q.id=t1.question_id AND q.type in (1,2,3,5)
INNER JOIN question_section t2 ON t1.question_id = t2.question_id AND t2.textbook_code in (:textbookCodes)
WHERE t1.student_id = :studentId  AND t1.status = 0 AND t1.mistake_num > 0 
GROUP BY SUBSTRING(t2.section_code, 1, 10), t1.question_id
) a GROUP BY a.sec1
#end

## 获取错题班级正确率
#macro($sfQuestionRateQuery(studentId, questionIds))
SELECT t.question_id as qid,AVG(t.right_rate) as rate,MAX(h.homework_class_id) FROM homework_question t
INNER JOIN student_homework sh ON sh.homework_id=t.homework_id AND sh.student_id=:studentId
INNER JOIN student_homework_question sq ON sq.student_homework_id=sh.id AND sq.question_id=t.question_id
INNER JOIN homework h on h.id=sh.homework_id
where t.question_id in (:questionIds)
group BY t.question_id
#end


## 统计教材题目错题数量
#macro($statisTextbookSfq(textbookCodes,userId,version))
SELECT COUNT(b.question_id) cou,b.textbook_code 
FROM student_fallible_question a INNER JOIN question_section b ON a.question_id = b.question_id 
WHERE b.textbook_code IN (:textbookCodes) AND a.student_id = :userId
AND a.type not in (2,6)
#if(version == 1)
	and b.v1 = 1
#end
#if(version == 2)
	and b.v2 = 1
#end
GROUP BY b.textbook_code
#end

## 根据学生以及题目id查询题目
#macro($queryByStudentAndQuestion(studentId,questionId))
SELECT t.* FROM student_fallible_question t WHERE t.student_id = :studentId AND t.question_id = :questionId
AND t.status = 0 AND t.mistake_num > 0
#end


## 查询章节对应易错知识点个数
#macro($queryWeakKpCount(studentId,sectionCodes))
	SELECT LEFT(b.section_code,10) section_code,COUNT(1) count0 FROM do_question_stu_kp_stat a 
	INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code AND LEFT(b.section_code,10) IN :sectionCodes
	inner join section c on b.section_code = c.code and c.name != '本章综合与测试'
	AND a.student_id = :studentId AND a.do_count > 0 AND (a.right_count+1)/(a.do_count+2) <= 0.6
	GROUP BY LEFT(b.section_code,10)
#end

## 查询章节下易错知识点相关信息
#macro($queryFallKpBySectionCodes(studentId,sectionCodes))
	  SELECT a.knowpoint_code,SUM(a.do_count) docount,SUM(a.right_count) rightcount,b.section_code
		FROM do_question_stu_kp_stat a 
  	INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code AND b.section_code IN :sectionCodes
  	WHERE a.student_id = :studentId AND a.do_count > 0  AND (a.right_count+1)/(a.do_count+2) <= 0.6
   	GROUP BY a.knowpoint_code,b.section_code
   	ORDER BY b.section_code	
#end

## 获取学生教材下易错知识点个数
#macro($getWeakKpCountByTextbookCodes(studentId,textbookCodes))
	SELECT COUNT(1) count0,c.textbook_code FROM do_question_stu_kp_stat a
   	INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code AND b.section_code
   	inner join section c on b.section_code = c.code and c.textbook_code in :textbookCodes
   	WHERE a.student_id = :studentId AND a.do_count > 0
   	AND (a.right_count+1)/(a.do_count+2) <= 0.6
   	group by c.textbook_code
#end

## 查询学生教材章节 错题数据,只查询到第一层并且去重
#macro($findStudentFallibleFirstSectionCount(studentId,categoryCode))
SELECT COUNT(a.coun) as amount,a.sec1 as scode,a.textbook_code FROM (
SELECT COUNT(t.id) coun,t.question_id,SUBSTRING(qs.section_code, 1, 10) AS sec1,qs.textbook_code
FROM student_fallible_question t 
INNER JOIN question_section qs ON t.question_id = qs.question_id AND qs.v2 = 1
WHERE t.student_id = :studentId AND qs.textbook_code LIKE :categoryCode AND t.status = 0 AND t.mistake_num > 0 
AND (t.type NOT IN (2, 4, 6) OR t.type IS NULL)
GROUP BY SUBSTRING(qs.section_code, 1, 10), t.question_id
) a GROUP BY a.sec1;
#end

## 查询学生教材错题数据,并且去重
#macro($findStudentFallibleTextbookCodeCount(studentId,textbookCodes))
SELECT COUNT(1) as amount,tt.textbook_code FROM (
SELECT COUNT(qs.question_id),qs.question_id,qs.textbook_code FROM student_fallible_question t
INNER JOIN question_section qs on qs.question_id = t.question_id AND qs.v2 = 1
WHERE t.student_id = :studentId
AND qs.textbook_code IN (:textbookCodes)
AND t.status = 0 
AND t.mistake_num > 0 
AND (t.type NOT IN (2, 4, 6) OR t.type IS NULL)
GROUP BY qs.question_id,qs.textbook_code
) tt GROUP BY tt.textbook_code
#end

## 查询其它版本下错题
#macro($zyQueryOtherCategoryCode(studentId,createAtCursor,categoryCode,dateParam))
SELECT sfq.* from student_fallible_question sfq 
LEFT JOIN question_section qs ON qs.question_id = sfq.question_id 
AND qs.v2 = 1
WHERE sfq.status = 0 
AND sfq.question_id IS NOT NULL
AND sfq.mistake_num > 0 
AND sfq.type NOT IN (2, 4, 6)
AND sfq.student_id = :studentId
AND sfq.update_at < :dateParam
#if(createAtCursor)
 AND sfq.update_at >= :createAtCursor
#end
AND sfq.id not in
(SELECT 
	DISTINCT(t.id)
FROM 
	student_fallible_question t 
LEFT JOIN question_section qs ON qs.question_id = t.question_id 
AND qs.v2 = 1
WHERE t.status = 0 
AND t.mistake_num > 0 
AND t.type NOT IN (2, 4, 6)
#if(createAtCursor)
 AND t.update_at >= :createAtCursor
#end
AND t.student_id = :studentId
AND qs.textbook_code LIKE :categoryCode)
GROUP BY sfq.id
ORDER BY sfq.update_at DESC
#end