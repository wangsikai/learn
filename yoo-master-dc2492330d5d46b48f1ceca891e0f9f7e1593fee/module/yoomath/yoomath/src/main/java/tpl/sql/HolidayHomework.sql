## 查询待布置的假期作业
#macro($queryNotPublishHomework(nowtime))
SELECT * FROM holiday_homework WHERE status = 0 AND start_time <= :nowtime AND id < :next AND del_status = 0 ORDER BY id DESC
#end

## 更新假期作业状态
#macro($updateStatus(id,status))
UPDATE holiday_homework SET status = :status WHERE id = :id
#end

##更新假期作业状态，已下发不能删除
#macro($updateHolidayHomework(homeworkId,teacherId))
UPDATE holiday_homework SET del_status = 1 WHERE id = :homeworkId AND create_id = :teacherId and status <= 2
#end

## 查询已经超过期限时间的假期作业
#macro($queryAfterDeadline(now))
SELECT * FROM holiday_homework WHERE status = 1 AND deadline <= :now AND id < :next AND del_status = 0 ORDER BY id DESC
#end

##查询作业v2.0.3(web v2.0)
#macro($queryHolidayHomeworkWeb2(createId,status,homeworkClassIds,homeworkClassId,keys,bt,et))
select DISTINCT h.* FROM holiday_homework h inner join holiday_homework_item t ON t.holiday_homework_id=h.id
#if(status)
	AND t.status in (:status)
#end
#if(homeworkClassIds)
	AND t.homework_class_id in (:homeworkClassIds)
#end
#if(homeworkClassId)
	AND t.homework_class_id =:homeworkClassId
#end
#if(bt)
	AND (t.deadline > :bt)
#end
#if(et)
	AND (t.start_time < :et)
#end
#if(keys)
	AND REPLACE(h.name,' ','') like :keys
#end
 where h.del_status = 0
 ORDER BY FIELD(h.STATUS,0,1,2,3), t.create_at DESC
#end

##获取假期作业专项数量
#macro($queryHolidayHomeworkItemCount(ids))
select t.holiday_homework_id as hid,COUNT(t.id) as hcount from holiday_homework_item t
 where t.holiday_homework_id in (:ids) 
 group by t.holiday_homework_id
#end

##获取最早发布的作业时间
#macro($getFirstCreateAt(teacherId, homeworkClassIds))
SELECT h1.start_time as ct FROM holiday_homework h1 
	WHERE h1.create_id=:teacherId AND h1.del_status = 0
#if(homeworkClassIds)
	AND h1.homework_class_id in (:homeworkClassIds)
#end
	ORDER BY h1.start_time ASC LIMIT 1
#end

##获取假期作业专项数量
#macro($countByCreateId(createId))
select count(*) from holiday_homework
 where create_id = :createId
#end

##查询全部假期作业
#macro($queryAllHolidayHomeworkByClazz(homeworkClassId))
select * from holiday_homework 
WHERE homework_class_id = :homeworkClassId and del_status != 1
#end

##删除全部假期作业
#macro($deleteAllHolidayHomeworkByClazz(homeworkClassId))
UPDATE holiday_homework SET del_status = 2 WHERE homework_class_id = :homeworkClassId 
and del_status != 1
#end