##获取假期活动数量
#macro($countHolidayActivity01(activityCode, teacherId, type))
select count(code) from holiday_activity_01
#end