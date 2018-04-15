#macro($zycQuery(schoolId,subjectCode))
SELECT * FROM school_question t WHERE
t.school_id = :schoolId
#if(subjectCode)
AND t.subject_code like :subjectCode
#end
#end

#macro($zycExists(schoolId,qCode))
SELECT count(*) FROM school_question s INNER JOIN question q on q.id = s.question_id
WHERE s.school_id = :schoolId AND q.code = :qCode
#end

## 根据学校id查找此学校的校本题目数量
#macro($countBySchoolId(schoolId))
SELECT count(*) FROM school_question t WHERE t.school_id = :schoolId
#end

## 根据学校id列表查找学校校题目数量
#macro($countBySchoolIds(schoolIds))
SELECT count(*) c, school_id AS schoolId FROM school_question t WHERE t.school_id IN :schoolIds GROUP BY school_id
#end