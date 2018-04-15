#macro($getKeyWordByType(keywordType,phaseCode,subjectCode))
SELECT k.word FROM keyword k
WHERE k.type=:keywordType AND k.phase_code = :phaseCode AND k.subject_code = :subjectCode AND NOT EXISTS (
 SELECT 1 FROM parameter WHERE VALUE = k.word
) ORDER BY k.times 
DESC,k.update_at DESC LIMIT 0,5 
#end

#macro($deleteAll())
DELETE FROM keyword
#end