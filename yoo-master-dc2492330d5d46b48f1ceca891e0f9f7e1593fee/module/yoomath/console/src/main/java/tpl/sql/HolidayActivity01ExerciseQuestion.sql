##删除所有错题、薄弱专项习题（练习初始化使用）
#macro($deleteAll(activityCode))
DELETE q FROM holiday_activity_01_exercise_question q
 INNER JOIN holiday_activity_01_exercise e ON e.id=q.holiday_activity_01_exercise_id AND e.type IN (1,2)
 WHERE q.activity_code=:activityCode AND q.id>99999
#end