#macro($getCountByCheckStaus(vendorId))
select count(id) count,status from question where vendor_id = :vendorId 
AND del_status != 2 AND sub_flag !=1 AND status!=1
group by status
#end

#macro($getKnowPoint(subject_code))
SELECT SUM(c1) total,SUM(c2) pass,SUM(c3) nopass,SUM(c4) editing ,SUM(c5) onepass ,meta_code FROM  (
		(SELECT COUNT(a.question_id) AS c1,0 AS c2,0 AS c3,0 AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id and b.status !=5
		 GROUP BY meta_code)
		 UNION 
		 (SELECT 0 AS c1,COUNT(a.question_id) AS c2,0 AS c3,0 AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.status =2
		 GROUP BY meta_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,COUNT(a.question_id) AS c3,0 AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =3
		 GROUP BY meta_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,0 AS c3,COUNT(a.question_id) AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =0
		 GROUP BY meta_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,0 AS c3,0 AS c4,COUNT(a.question_id) AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =4
		 GROUP BY meta_code
		 )
	) tt  GROUP BY meta_code
#end

## 获取校验题目
#macro($getCheckQuestions(vendorUserId, vendorId, hasCheckOne, hasCheckTwo, questionIds, phaseCode, subjectCode, questionType, createBt, createEt, vuser, knowpoints, paperId, bookVersionId,trainId, checkRefund))
SELECT t.id from question t
#if(knowpoints)
 inner join question_knowledge qm on qm.question_id=t.id and qm.knowledge_code in (:knowpoints)
#end
#if(paperId)
 inner join exam_paper_question ep on ep.question_id=t.id and ep.exam_paper_id=:paperId
#end
#if(trainId)
 inner join special_training_question tq on tq.question_id=t.id and tq.special_training_id=:trainId
#end
#if(bookVersionId)
 inner join book_question bq on bq.question_id=t.id
 inner join book_version bv on bv.id=bq.book_version_id and bv.id=:bookVersionId and bv.status!=5
#end
where (t.subject_code = 202 or t.subject_code = 302) and t.create_id!=:vendorUserId and t.vendor_id=:vendorId
#if(questionIds)
 and t.id not in (:questionIds)
#end
#if(phaseCode)
 and t.phase_code=:phaseCode
#end
#if(subjectCode)
 and t.subject_code=:subjectCode
#end
#if(questionType)
 and t.type=:questionType
#end
#if(createBt)
 and t.create_at>=:createBt and t.create_at<:createEt
#end
#if(vuser)
 and t.create_id=:vuser
#end
 and t.sub_flag !=1 AND t.del_status=0
 #if(hasCheckOne == true && hasCheckTwo == false)
  AND t.status = 0
 #end
 #if(hasCheckOne == false && hasCheckTwo == true)
  AND t.status = 4
 #end
 #if(hasCheckOne == true && hasCheckTwo == true)
  AND (t.status = 0 OR (t.status = 4 AND (t.verify_id!=:vendorUserId OR t.verify_id IS NULL)))
 #end
 #if(paperId == null && bookVersionId == null)
  AND t.same_show_id is null
 #end
 #if(checkRefund == true)
  AND t.check_refund=1 and t.verify_id=:vendorUserId
 #end
 #if(checkRefund == false)
  AND !(check_refund=1 and t.verify_id!=:vendorUserId) order by t.check_refund ASC
 #end
#end

## 判断书本题目是否完全校验通过
#macro($checkBookComplete(bookId))
SELECT COUNT(t.id) FROM question t
 INNER JOIN book_question bq ON bq.question_id=t.id 
 INNER JOIN book_version bv ON bv.id=bq.book_version_id AND bv.status!=5 AND bv.book_id=:bookId
 WHERE t.status != 2 AND t.del_status != 1
#end
 
 ## 判断试卷题目是否完全校验通过
#macro($checkPaperComplete(paperId))
SELECT COUNT(t.id) FROM question t
 INNER JOIN exam_paper_question ep ON ep.question_id=t.id AND ep.exam_paper_id=:paperId
 WHERE t.status != 2 AND t.del_status != 1
#end


 ## 判断训练题目是否完全校验通过
#macro($checkTrainComplete(trainId))
SELECT COUNT(t.id) FROM question t
 INNER JOIN special_training_question tq ON tq.question_id=t.id AND tq.special_training_id=:trainId
 WHERE t.status != 2 AND t.del_status != 1
#end


##获取需要被转换的题目（根据题目状态已经取出来没有被释放的题目）
#macro($getConvertQuestion(asciiStatus,questionIds))
select a.* from (
SELECT t.* FROM question t where 1=1 
#if(asciiStatus)
#if(asciiStatus==0)
AND (t.ascii_status is NULL OR t.ascii_status =:asciiStatus)
#end
#if(asciiStatus==1)
AND t.ascii_status =:asciiStatus
#end
#end
#if(questionIds)
AND t.id NOT IN (:questionIds)
#end
AND (t.subject_code =302 OR t.subject_code = 202) AND t.type = 3 AND t.sub_flag = 0 AND t.status= 2 limit 1
union
SELECT t.* FROM question t 
inner join question t2 on t2.id=t.parent_id and t2.status= 2
where 1=1 
#if(asciiStatus)
#if(asciiStatus==0)
AND (t.ascii_status is NULL OR t.ascii_status =:asciiStatus)
#end
#if(asciiStatus==1)
AND t.ascii_status =:asciiStatus
#end
#end
#if(questionIds)
AND t.id NOT IN (:questionIds)
#end
AND (t.subject_code =302 OR t.subject_code = 202) AND t.type = 3 AND t.sub_flag = 1 limit 1
) a limit 1
#end

##获取题目（不包含子题）统计
#macro($getConvertQuestionCount())
SELECT  COUNT(CASE WHEN ascii_status = 0 AND TYPE = 3 THEN 1 ELSE NULL END) noChangeCount,
		COUNT(CASE WHEN ascii_status = 0 AND TYPE = 3 AND STATUS=2 THEN 1 ELSE NULL END) noChangePassCount,
        COUNT(CASE WHEN ascii_status = 1 AND TYPE = 3 AND STATUS=2 THEN 1 ELSE NULL END) noCheckCount
	FROM question WHERE sub_flag = 0
#end

##获取题目（仅包含子题）统计
#macro($getConvertQuestionCountFromSub())
SELECT  COUNT(CASE WHEN q1.ascii_status = 0 AND q1.TYPE = 3 THEN 1 ELSE NULL END) noChangeCount,
		COUNT(CASE WHEN q1.ascii_status = 0 AND q1.TYPE = 3 AND q2.STATUS=2 THEN 1 ELSE NULL END) noChangePassCount,
        COUNT(CASE WHEN q1.ascii_status = 1 AND q1.TYPE = 3 AND q2.STATUS=2 THEN 1 ELSE NULL END) noCheckCount
	FROM question q1
	INNER JOIN question q2 ON q2.id = q1.parent_id
	WHERE q1.sub_flag = 1
#end

##获取新知识点更新题目（主题）统计
#macro($calNewKnowledgeDatas(userId, day, vendorId, typeCodes))
SELECT COUNT(t.id) AS c FROM question t WHERE t.knowledge_create_id=:userId and t.del_status=0 and t.sub_flag=0 and t.vendor_id=:vendorId and t.type != 2 and t.type != 4
 UNION ALL
 SELECT COUNT(t.id) AS c FROM question t WHERE t.knowledge_create_id=:userId and t.vendor_id=:vendorId AND DATE_FORMAT(t.knowledge_create_at, '%Y%m%d')=:day and t.del_status=0 and sub_flag=0 and t.type != 2 and t.type != 4
 UNION ALL
 SELECT COUNT(t.id) AS c FROM question t WHERE t.knowledge_create_id>0 AND DATE_FORMAT(t.knowledge_create_at, '%Y%m%d')=:day and t.vendor_id=:vendorId and t.del_status=0 and sub_flag=0 and t.type != 2 and t.type != 4
 UNION ALL
 SELECT COUNT(t.id) AS c FROM question t WHERE t.vendor_id=:vendorId AND t.del_status=0 and t.sub_flag=0 
  AND t.type_code IN (:typeCodes) and t.status != 5
  AND NOT EXISTS (SELECT 1 FROM question_knowledge WHERE question_id=t.id)
 UNION ALL
 SELECT COUNT(t.id) AS c FROM question t WHERE t.vendor_id=:vendorId AND t.del_status=0 and t.sub_flag=0 
  AND t.type_code IN (:typeCodes) and t.status != 5
  AND EXISTS (SELECT 1 FROM question_knowledge WHERE question_id=t.id)
#end

##获取旧知识点下未处理题目个数（第一级）
#macro($calNoKnowledgeL1(subjectCode, typeCodes, vendorId))
SELECT k2.pcode as code,COUNT(DISTINCT(t.question_id)) as counts FROM question_metaknow t
 INNER JOIN meta_knowpoint mk ON mk.code=t.meta_code AND mk.subject_code=:subjectCode
 INNER JOIN metaknow_know mkn ON mkn.meta_code=mk.code
 INNER JOIN knowpoint k2 ON k2.code=mkn.know_point_code
 INNER JOIN question q ON q.id = t.question_id
 AND q.del_status=0 AND q.sub_flag=0 AND q.type_code IN (:typeCodes) 
 AND q.vendor_id=:vendorId AND q.status != 5 AND NOT EXISTS (SELECT 1 FROM question_knowledge WHERE question_id=q.id)
 GROUP BY k2.pcode
#end

##获取新知识点已未处理题目个数（第一级）
#macro($calHasKnowledgeL1(subjectCode, typeCodes, vendorId))
SELECT ks2.pcode as code,COUNT(DISTINCT(t.question_id)) as counts FROM question_knowledge t
 INNER JOIN knowledge_point kp ON kp.code=t.knowledge_code AND kp.subject_code=:subjectCode
 INNER JOIN knowledge_system ks3 ON ks3.code=kp.pcode
 INNER JOIN knowledge_system ks2 ON ks2.code=ks3.pcode
 INNER JOIN question q ON q.id = t.question_id
 AND q.del_status=0 AND q.sub_flag=0 AND q.type_code IN (:typeCodes) 
 AND q.vendor_id=:vendorId
 GROUP BY ks2.pcode
#end

## 根据code查找数据 copy -> console/Question.sql
#macro($resconFindQuestionByCode(codes,status))
SELECT q.* FROM question q WHERE q.code IN (:codes)
#if(status)
and status = 2
#end
#end

## 查询需要重建习题章节的习题列表
#macro($queryReQuestionSection())
SELECT q.* FROM question q WHERE q.status=2 AND q.del_status=0 AND q.verify2_at > '2016-11-23'
order by q.id ASC
#end

## 查询需要更新V3知识点的习题个数（去除重复题）
#macro($getNoHasV3KPQuestionCount(vendorId, phaseCode))
SELECT COUNT(q.id) FROM question q
LEFT JOIN question_knowledge_sync qs ON qs.question_id = q.id
LEFT JOIN question_knowledge_review qr ON qr.question_id = q.id
WHERE q.status=2 AND q.del_status=0 AND q.sub_flag=0 AND q.type IN (1,3,5) AND q.vendor_id=:vendorId AND q.same_show_id IS NULL
AND qs.question_id IS NULL AND qr.question_id IS NULL
#if(phaseCode)
 AND q.phase_code=:phaseCode
#end
#end

## 查询同步知识点的习题个数
#macro($getSyncKPQuestionCount(kp, phaseCode))
SELECT COUNT(DISTINCT(qks.question_id)) FROM question_knowledge_sync qks
#if(phaseCode)
inner join knowledge_sync ks on ks.code=qks.knowledge_code and ks.phase_code=:phaseCode
#end
INNER JOIN question q ON q.id=qks.`question_id` AND q.`status`=2 AND q.`del_status`=0 AND q.`type` IN (1,3,5)
#if(kp)
 where qks.knowledge_code like :kp
#end
#end

## 查询复习知识点的习题个数
#macro($getReviewKPQuestionCount(kp, phaseCode))
SELECT COUNT(DISTINCT(qkr.question_id)) FROM question_knowledge_review qkr
#if(phaseCode)
inner join knowledge_review kr on kr.code=qkr.knowledge_code and kr.phase_code=:phaseCode
#end
INNER JOIN question q ON q.id=qkr.`question_id` AND q.`status`=2 AND q.`del_status`=0 AND q.`type` IN (1,3,5)
#if(kp)
 where qkr.knowledge_code like :kp
#end
#end