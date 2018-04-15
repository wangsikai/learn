## 查询活动用户记录的教材和年级
#macro($queryHolidayActivity01UserCategoryGrade(code,userId))
select * from holiday_activity_01_user_category_grade 
WHERE activity_code =:code AND user_id =:userId 
#end

## 更新活动用户记录的教材和年级
#macro($updateHolidayActivity01UserCategoryGrade(id,grade,category))
update holiday_activity_01_user_category_grade 
set textbook_category_code=:category,grade=:grade
WHERE id=:id
#end
