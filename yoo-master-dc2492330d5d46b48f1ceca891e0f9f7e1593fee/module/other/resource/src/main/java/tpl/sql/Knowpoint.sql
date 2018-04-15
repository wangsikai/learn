#macro($findLastestCode(pcode, subjectCode))
SELECT * FROM knowpoint t
WHERE
t.subject_code = :subjectCode
#if(pcode)
AND t.pcode = :pcode
#end
ORDER BY t.code DESC LIMIT 1
#end

#macro($findByPSAndPcode(pcode, subjectCode, phaseCode))
SELECT * FROM knowpoint t
WHERE 1=1
#if(pcode)
AND t.pcode = :pcode
#end
AND t.phase_code = :phaseCode AND t.subject_code = :subjectCode
ORDER BY code ASC
#end

##通过关键字和学科查找知识点
#macro($listBySubjectAndKey(subjectCode,name))
SELECT k.code,k.name,k.pcode,k.status,k.subject_code,k.level,k.phase_code,k.sequence FROM knowpoint k WHERE subject_code =:subjectCode and status=0
#if(name)
AND k.name like :name
#end
UNION 
SELECT mkp.code,mkp.name,mk.know_point_code AS pcode,mkp.status,mkp.subject_code,k.level+1 AS LEVEL,mkp.phase_code, mkp.sequence FROM `meta_knowpoint` mkp
INNER JOIN `metaknow_know` mk ON mk.meta_code = mkp.code
INNER JOIN knowpoint k ON k.code = mk.know_point_code AND k.subject_code =:subjectCode 
#if(name)
AND (k.name LIKE :name OR mkp.name LIKE :name)
#end
AND k.status = 0 AND mkp.status = 0
#end


##通过关键字和学科查找新知识点
#macro($newlistBySubjectAndKey(subjectCode,name))
select code,name,pcode,status,subject_code,level,phase_code,sequence from knowledge_system WHERE subject_code =202
UNION
SELECT kp.code,kp.name,kp.pcode,kp.status,kp.subject_code,ks.level +1 as level,kp.phase_code,kp.sequence FROM knowledge_point kp 
INNER JOIN knowledge_system ks ON ks.code =kp.pcode
#end





##将待启用转为已启用
#macro($resconTurnOn())
UPDATE knowpoint SET status = 0 WHERE status = 1
#end