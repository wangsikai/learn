##通过关键字和学科查找知识点
#macro($listBySubjectAndKey(subjectCode,name))
select code,name,pcode,status,subject_code,level,phase_code,sequence from knowledge_system 
WHERE subject_code =:subjectCode and status=0
#if(name)
AND name like :name
#end
UNION
SELECT kp.code,kp.name,kp.pcode,kp.status,kp.subject_code,ks.level +1 as level,kp.phase_code,kp.sequence FROM knowledge_point kp 
INNER JOIN knowledge_system ks ON ks.code =kp.pcode and ks.subject_code=:subjectCode
#if(name)
AND (kp.name LIKE :name)
#end
AND kp.status = 0 and ks.status=0 and kp.subject_code=:subjectCode
#end


##根据条件查询知识体系
#macro($findAll(phaseCode,subjectCode,pcode,lv))
SELECT * FROM knowledge_system WHERE 1=1
#if(pcode)
 and pcode =:pcode 
#end
#if(lv)
 and level =:lv
#end
#if(subjectCode)
 and subject_code = :subjectCode
#end
#if(phaseCode)
 and phase_code = :phaseCode
#end
#end

##根据条件查询知识体系统计
#macro($getStat(phaseCode,subjectCode))
SELECT COUNT(1) count,level FROM knowledge_system 
where phase_code = :phaseCode and subject_code =:subjectCode
GROUP BY level
#end

##根据条件查询小专题下知识体系
#macro($findSmallSpecial(phaseCode,subjectCode,knowpointCode))
SELECT * FROM knowledge_system WHERE pcode = :knowpointCode
	#if(subjectCode)
	 and subject_code = :subjectCode
	#end
	#if(phaseCode)
	 and phase_code = :phaseCode
	#end
#end

##模糊匹配
#macro($findByCode(code))
SELECT * FROM knowledge_system 
where code like :code
#end
