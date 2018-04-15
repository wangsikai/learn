#macro($findBySubject(subjectCode, typeCode))
SELECT t.* FROM question_type t
#if(typeCode)
 inner join question_base_type b on b.question_code=t.code AND b.base_code=:typeCode
#end
 WHERE t.subject_code = :subjectCode ORDER BY t.sequence ASC
#end