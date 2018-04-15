#macro($zycFindByName(schoolName))
SELECT t.* FROM question_school t INNER JOIN school s ON t.school_id = s.id
WHERE (t.status = 0 OR t.status = 1)
#if(schoolName)
AND s.name LIKE :schoolName
#end
ORDER BY t.create_at DESC
#end