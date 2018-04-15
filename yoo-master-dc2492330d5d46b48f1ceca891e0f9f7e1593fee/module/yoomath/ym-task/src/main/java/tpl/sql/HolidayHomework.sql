## 查询一个班级当天的数据
#macro($ymFindHolidayHk(classId,beginDate,endDate))
SELECT t.* FROM holiday_homework t WHERE t.status = 2 AND t.del_status = 0 AND t.homework_class_id = :classId
#if(beginDate)
  AND t.start_time >= :beginDate
#end
#if(endDate)
  AND t.start_time <= :endDate
#end
ORDER BY t.start_time ASC
#end