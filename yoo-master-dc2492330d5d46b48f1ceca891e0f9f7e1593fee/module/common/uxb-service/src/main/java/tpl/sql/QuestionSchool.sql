#macro($findBySchoolId(schoolId))
SELECT * FROM question_school t where t.school_id = :schoolId
#end