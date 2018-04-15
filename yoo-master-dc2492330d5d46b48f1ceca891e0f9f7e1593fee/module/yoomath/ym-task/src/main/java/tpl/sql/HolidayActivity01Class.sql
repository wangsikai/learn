##重置班级提交率
#macro($resetClassSubmitRate(ids))
update holiday_activity_01_class set submit_rate = 0 where id in (:ids)
#end


##统计班级提交率
#macro($taskHolidayActivity01StatisticClazzs(activityCode,startPeriodTime,endPeriodTime,classId))
select hah.id,hah.user_id,hah.activity_code,hah.class_id,count(1) as homeworkCount,ROUND(avg(hah.submit_rate), 0) as avgSubmitRate,ROUND(avg(hk.right_rate), 0) as avgRightRate  from 
holiday_activity_01_homework hah 
INNER JOIN homework hk on homework_id =hk.id  where hah.activity_code=:activityCode and hah.class_id>:next 
and hah.homework_id is not null 
#if(startPeriodTime)
and hah.create_at >:startPeriodTime 
#end
#if(endPeriodTime)
and hah.create_at <=:endPeriodTime
#end
#if(classId)
and hah.class_id=:classId
#end
GROUP BY  hah.class_id 
#if(classId)
ORDER BY  hah.id ASC
#end
#end


##分页获取假期班级
#macro($findClassList())
SELECT id FROM holiday_activity_01_class WHERE id < :next ORDER BY id DESC
#end

##批量获取假期班级
#macro($mgetHolidayActivity01Class(ids))
SELECT * FROM holiday_activity_01_class WHERE id in (:ids)
#end
