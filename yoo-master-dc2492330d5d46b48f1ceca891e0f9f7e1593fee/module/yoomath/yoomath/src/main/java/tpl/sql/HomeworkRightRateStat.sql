## 根据classId获取班级按周统计的平均正确率
#macro($getStat(classId,startTime,endTime))
SELECT * FROM homework_rightrate_stat where homework_class_id = :classId
and statistics_time > :startTime and statistics_time < :endTime
#end
