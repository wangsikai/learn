## [供应商]
#macro($vQuery(subjectCodes, phaseCodes, key, status))
SELECT t.* from meta_knowpoint t where 1=1
#if(subjectCodes)
 AND t.subject_code in (:subjectCodes)
#end
#if(phaseCodes)
 AND t.phase_code in (:phaseCodes)
#end
#if(key)
 AND t.name like :key
#end
#if(status)
 AND t.status in (:status)
#end
 ORDER BY t.name ASC
#end

## 根据科目查询
#macro($listBySubject(subjectCode, key))
SELECT t.* from meta_knowpoint t where t.subject_code=:subjectCode and t.status=0
#if(key)
 AND t.name like :key
#end
 ORDER BY t.name ASC
#end

## 根据科目查询
#macro($listBySubject2(subjectCode, key))
SELECT m.know_point_code AS pId,t.code AS id,t.name as name FROM meta_knowpoint t
INNER JOIN `metaknow_know` m ON m.meta_code=t.code
where t.subject_code=:subjectCode and t.status=0
#end

#macro($listByKnowpoint(knowPointCode))
select t.* from meta_knowpoint t INNER join metaknow_know m on m.meta_code = t.code
INNER  JOIN knowpoint k on m.know_point_code = k.code
where k.code = :knowPointCode and t.status = 0
#end