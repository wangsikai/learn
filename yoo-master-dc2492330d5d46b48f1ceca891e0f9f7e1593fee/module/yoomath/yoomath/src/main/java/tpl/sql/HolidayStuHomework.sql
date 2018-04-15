##查询学生寒假作业信息
#macro($queryStuHomework(holidayHomeworkId,stuIds))
SELECT * FROM holiday_stu_homework WHERE holiday_homework_id =:holidayHomeworkId and student_id in(:stuIds)
#end

## 查询学生假期作业按正确率倒序且正确率不得为空
#macro($findStuHkByHk(hkId))
SELECT t.* FROM holiday_stu_homework t WHERE t.holiday_homework_id = :hkId
AND t.right_rate IS NOT NULL ORDER BY t.right_rate DESC
#end


##更新学生作业表状态
#macro($updateStuHolidayHomework(homeworkId))
UPDATE holiday_stu_homework  SET del_status = 1 where holiday_homework_id = :homeworkId
#end

##更新学生作业正确率
#macro($uptStuHomeworkCompleteRate(id,completeRate))
UPDATE holiday_stu_homework  SET completion_rate = :completeRate where id = :id
#end

## 更新为完成状态
#macro($updateStatus(hdHkId,status))
UPDATE holiday_stu_homework SET status = :status WHERE holiday_homework_id = :hdHkId
#end

##获取某个学生未完成寒假作业的数量
#macro($countNotSubmit(studentId))
SELECT 
	count(id) 
FROM holiday_stu_homework_item 
WHERE del_status = 0 AND status = 0 AND student_id = :studentId
#end

## 更新已经下发的作业查看状态
#macro($updateViewStatus(id))
UPDATE holiday_stu_homework SET viewed = 1 WHERE id = :id
#end


##查询作业v2.0.3(web v2.0)
#macro($queryHolidayHomeworkWeb(studentId,status,homeworkClassIds,homeworkClassId,keys,bt,et,statusIndex))
SELECT DISTINCT hsh.* FROM holiday_stu_homework hsh
 INNER JOIN holiday_stu_homework_item item ON item.holiday_stu_homework_id=hsh.id
 INNER JOIN holiday_homework h ON h.id = item.holiday_homework_id
 INNER JOIN holiday_homework_item t ON t.id = item.holiday_homework_item_id
 where hsh.student_id=:studentId and h.del_status = 0
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
#if(statusIndex)
	#if(statusIndex == 1)
		AND hsh.status=2
	#end
	#if(statusIndex == 2)
		AND hsh.status=0
	#end
	#if(statusIndex == 3)
		AND hsh.status=1
	#end
	#if(statusIndex == 4)
		AND hsh.status=1
	#end
#end
 ORDER BY hsh.status ASC, hsh.create_at DESC
#end

##获取最早开始的作业时间
#macro($getFirstStartAt(studentId, homeworkClassIds))
SELECT h1.start_time as ct FROM holiday_homework h1 
 inner join holiday_stu_homework hsh on hsh.holiday_homework_id=h1.id and hsh.student_id=:studentId
	WHERE h1.del_status = 0
#if(homeworkClassIds)
	AND h1.homework_class_id in (:homeworkClassIds)
#end
	ORDER BY h1.start_time ASC LIMIT 1
#end

##删除全部学生作业
#macro($deleteStuHolidayHomeworkByClazz(homeworkId))
UPDATE holiday_stu_homework  SET del_status = 2 
where holiday_homework_id IN (:holidayids)
and del_status != 1
#end