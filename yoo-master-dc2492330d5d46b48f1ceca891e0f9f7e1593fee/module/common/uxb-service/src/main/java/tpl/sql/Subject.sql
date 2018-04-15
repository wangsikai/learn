#macro($findAllSubject())
SELECT * FROM subject WHERE status = 0 ORDER BY sequence ASC,code ASC
#end

#macro($getSubjectsByPhaseCode(phaseCode))
SELECT * FROM subject WHERE status = 0
#if(phaseCode)
AND phase_code = :phaseCode
#end
ORDER BY sequence ASC,code ASC
#end