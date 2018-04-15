## 查询一条时间段内记录
#macro($taskGetStatisticOneData(activityCode,startDate,endDate,clazzId))
select * from holiday_activity_01_statistics where activity_code =:activityCode 
and end_period_time=:endDate
#if(startDate)
and start_period_time =:startDate 
#end
#if(clazzId)
and class_id=:clazzId
#end
limit 1
#end

## 删除时间段内统计数据
#macro($taskDeleteStatistic(activityCode,startDate,endDate))
DELETE  from holiday_activity_01_statistics where activity_code =:activityCode 
and start_period_time =:startDate 
and end_period_time=:endDate
#end