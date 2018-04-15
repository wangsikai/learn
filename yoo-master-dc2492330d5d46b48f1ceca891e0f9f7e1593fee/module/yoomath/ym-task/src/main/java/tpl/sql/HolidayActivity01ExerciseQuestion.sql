## 查询活动练习
#macro($taskQueryHolidayActivity01ExerciseQuestion(exerciseIds))
select t.* from holiday_activity_01_exercise_question t 
WHERE t.holiday_activity_01_exercise_id IN (:exerciseIds)  
ORDER BY t.holiday_activity_01_exercise_id,t.sequence
#end
