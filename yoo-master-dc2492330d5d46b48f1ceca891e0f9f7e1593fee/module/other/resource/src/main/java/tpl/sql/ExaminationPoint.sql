## 查找考点集合
#macro($find(phaseCode, subjectCode, pcode, status))
select t.* from examination_point t 
WHERE status!=1
#if(phaseCode)
 AND t.phase_code = :phaseCode
#end
#if(subjectCode)
 AND t.subject_code = :subjectCode
#end
#if(pcode)
 AND t.pcode=:pcode
#end
#if(status)
 AND t.status=:status
#end
union
select t.* from examination_point t 
WHERE status=1
#if(phaseCode)
 AND t.phase_code = :phaseCode
#end
#if(subjectCode)
 AND t.subject_code = :subjectCode
#end
#if(pcode)
 AND t.pcode=:pcode
#end
#if(status)
 AND t.status=:status
#end
#end

## 查找考点集合(已启用)
#macro($findUse(phaseCode, subjectCode))
select t.* from examination_point t
WHERE t.phase_code = :phaseCode AND t.subject_code = :subjectCode AND t.status = 0
#end

## 查找考点统计(已启用)
#macro($queryCounts(phaseCode, subjectCode))
SELECT SUM(tt.ebnum) as ebnum,SUM(tt.kpnum) as kpnum FROM (
 SELECT COUNT(t.id) AS ebnum,0 AS kpnum FROM examination_point t
 WHERE t.`status` = 0 and t.phase_code = :phaseCode AND t.subject_code = :subjectCode
 UNION
 SELECT 0 AS ebnum, SUM(t.knowpoint_count) AS kpnum FROM examination_point t
 WHERE t.`status` = 0 and t.phase_code = :phaseCode AND t.subject_code = :subjectCode
) tt
#end

## 通过考点集合获取考点对象
#macro($queryQuestionsByExamIds(examIds))
select * from examination_point  where id in (:examIds)
#end

## 查找考点集合
#macro($listBySmallSpecailCode(phaseCode, subjectCode,knowpointCode))
select t.* from examination_point t 
WHERE t.phase_code = :phaseCode AND t.subject_code = :subjectCode and t.status = 0
	and t.pcode in (select code from knowledge_system where pcode =:knowpointCode)
#end

## 查找所有考点还未启用的
#macro($findAllEditing())
SELECT t.* FROM examination_point t WHERE t.status = 2
#end

## 查找包含某个典型习题的考点数量（删除典型题时使用）
#macro($countEPByQuestion(questionId, eliminateId))
select count(t.id) from examination_point t 
where t.questions like :questionId and t.id!=:eliminateId and t.status=0
#end