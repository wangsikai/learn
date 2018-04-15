##查询有效暑期活动
#macro($taskHolidayActivityGetAll(now))
select * from holiday_activity_01 where status =0 and start_time<=:now and end_time>=:now
#end

