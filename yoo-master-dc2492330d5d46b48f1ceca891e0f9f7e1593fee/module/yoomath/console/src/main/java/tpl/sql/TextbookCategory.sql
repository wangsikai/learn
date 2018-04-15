#macro($getMathTextList(subjectCode,yoomathStatus,highStatus,middleStatus,primaryStatus))
SELECT a.code vcode,a.name vname,a.sequence vsequence,a.high_status hstatus,a.middle_status mstatus,a.primary_status pstatus,
		b.code tcode,b.name tname,b.sequence tsequence ,b.yoomath_status tstatus,b.phase_code
FROM textbook_category a INNER JOIN textbook b ON a.code = b.category_code
WHERE b.subject_code =:subjectCode
#if(yoomathStatus)
	and a.yoomath_status =:yoomathStatus
	and b.yoomath_status =:yoomathStatus
#end
#if(highStatus)
	and a.high_status =:highStatus
#end
#if(middleStatus)
	and a.middle_status =:middleStatus
#end
#if(primaryStatus)
	and a.primary_status =:primaryStatus
#end
#end

