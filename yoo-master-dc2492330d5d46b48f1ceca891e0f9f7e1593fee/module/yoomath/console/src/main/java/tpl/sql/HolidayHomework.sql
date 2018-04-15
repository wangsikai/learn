##得到待处理的作业列表
#macro($zycGetTodo(startTime,endTime,schoolName))
SELECT t.* FROM holiday_homework t
INNER JOIN homework_class c ON c.id = t.homework_class_id
INNER JOIN teacher u ON t.create_id = u.id
LEFT JOIN school s ON u.school_id = s.id
WHERE del_status =0
#if(schoolName)
  AND s.name LIKE :schoolName
#end
#if(startTime)
  AND t.start_time >= :startTime
#end
#if(endTime)
  AND t.start_time <= :endTime
#end
ORDER BY t.start_time DESC
#end

##查询作业下发数量
#macro($zycGetDistribute(ids))
select t.holiday_homework_id as id,count(1) distribute from holiday_stu_homework t where t.holiday_homework_id in :ids
GROUP BY t.holiday_homework_id
#end

##查询学生专项提交数量
#macro($zycGetItemSubmitCount(ids))
select t.holiday_homework_id as id ,count(1) as submitCount 
from holiday_stu_homework_item t where status =1 and t.holiday_homework_id in :ids
GROUP BY t.holiday_homework_id
#end

##作业转让,没有下发的作业更新创建人
#macro($zycHolidayHkTransfer(classId,newTeacherId))
	update holiday_homework set create_id = :newTeacherId where homework_class_id = :classId and status != 3
#end


