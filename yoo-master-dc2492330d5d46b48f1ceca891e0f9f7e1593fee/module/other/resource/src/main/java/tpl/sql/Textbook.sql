#macro($findLastestCode(categoryCode, phaseCode, subjectCode))
SELECT * FROM textbook t WHERE t.category_code = :categoryCode AND t.phase_code = :phaseCode
AND t.subject_code = :subjectCode ORDER BY t.code DESC limit 1
#end