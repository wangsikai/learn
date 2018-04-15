#macro($opQuery(phaseCode,subjectCode))
SELECT id FROM question WHERE del_status = 0 AND phase_code = :phaseCode AND subject_code = :subjectCode
#if(next)
	AND id < :next
#end
ORDER BY id DESC
#end

#macro($opRemaining(id,phaseCode,subjectCode))
SELECT count(id) FROM question WHERE del_status = 0  AND phase_code = :phaseCode AND subject_code = :subjectCode AND id < :id
#end
