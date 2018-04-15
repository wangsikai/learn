#macro($findByPcode(pcode))
SELECT * FROM knowpoint WHERE status = 0 AND pcode = :pcode
#end

#macro($getParent(code))
SELECT * FROM knowpoint WHERE (SELECT pcode FROM knowpoint WHERE code = :code) AND status = 0
#end

#macro($listBySubject(subjectCode))
SELECT * FROM knowpoint WHERE subject_code=:subjectCode AND status = 0
#end

#macro($listAllBySubject(subjectCode))
SELECT k.code,k.name,k.pcode,k.status,k.subject_code,k.level,k.phase_code,k.sequence FROM knowpoint k WHERE subject_code =:subjectCode AND k.status = 0
UNION 
SELECT mkp.code,mkp.name,mk.know_point_code AS pcode,mkp.status,mkp.subject_code,k.level+1 AS LEVEL,mkp.phase_code, mkp.sequence FROM `meta_knowpoint` mkp
INNER JOIN `metaknow_know` mk ON mk.meta_code = mkp.code
INNER JOIN knowpoint k ON k.code = mk.know_point_code AND k.subject_code =:subjectCode
WHERE mkp.status = 0 AND k.status = 0
#end
