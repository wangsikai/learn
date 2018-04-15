## 查询活动练习
#macro($queryHolidayActivity01ExerciseQuestion(activityCode,exerciseIds))
select t.* from holiday_activity_01_exercise_question t 
WHERE activity_code =:activityCode
#if(exerciseIds)
AND t.holiday_activity_01_exercise_id IN (:exerciseIds)  
#end
ORDER BY t.holiday_activity_01_exercise_id,t.sequence
#end
