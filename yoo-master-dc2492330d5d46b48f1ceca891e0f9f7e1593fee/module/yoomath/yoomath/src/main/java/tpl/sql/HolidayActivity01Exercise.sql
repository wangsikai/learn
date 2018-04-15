## 游标查询活动练习
#macro($listHolidayActivity01Exercise(activityCode,categoryCode,userId,type,grade))
select t.* from holiday_activity_01_exercise t where activity_code=:activityCode AND t.type=:type
#if(categoryCode)
AND t.textbook_category_code =:categoryCode
#end
#if(userId)
AND t.user_id =:userId
#end
#if(grade)
AND t.grade =:grade
#end
AND t.sequence > :next
order by t.sequence ASC
#end