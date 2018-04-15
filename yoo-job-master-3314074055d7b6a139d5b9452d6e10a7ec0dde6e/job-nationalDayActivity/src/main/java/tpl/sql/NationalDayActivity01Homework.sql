#macro($nda01Delete(homeworkId))
DELETE FROM national_day_activity_01_homework WHERE homework_id = :homeworkId
#end

#macro($nda01RandomGetOne())
SELECT * FROM national_day_activity_01_homework LIMIT 1;
#end

#macro($nda01findHomeworkTeacherIdByUsers(userIds))
SELECT DISTINCT teacher_id 
FROM national_day_activity_01_homework 
WHRER teacher_id IN (:userIds)
#end

## 查询teacherIds
#macro($nda01findTeacherIdsSpecify(startindex,size))
SELECT DISTINCT teacher_id FROM national_day_activity_01_homework 
LIMIT :startindex, :size
#end

#macro($nda01findHomeworkByUsers(teacherIds))
SELECT *
FROM national_day_activity_01_homework 
WHERE teacher_id IN (:teacherIds)
#end