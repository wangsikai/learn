##查询知识点
#macro($findPoint(phaseCode,subjectCode,pcode,status))
SELECT * FROM knowledge_point WHERE pcode =:pcode
	#if(subjectCode)
		and subject_code = :subjectCode
	#end
	#if(phaseCode)
		and phase_code = :phaseCode
	#end
	#if(status)
		and status = :status
	#end
#end

##查询知识点
#macro($getPointCount(phaseCode,subjectCode))
SELECT count(code) FROM knowledge_point WHERE subject_code = :subjectCode and phase_code = :phaseCode
#end

##查询所有知识点
#macro($findAll(phaseCode, subjectCode,knowpointCode, status))
SELECT * FROM knowledge_point WHERE 1 = 1
	#if(subjectCode)
		and subject_code = :subjectCode
	#end
	#if(phaseCode)
		and phase_code = :phaseCode
	#end
	#if(status)
		and status = :status
	#end
	#if(knowpointCode)
		and pcode = :knowpointCode
	#end
#end

##模糊匹配
#macro($findByCode(code))
SELECT * FROM knowledge_point 
where code like :code
#end

