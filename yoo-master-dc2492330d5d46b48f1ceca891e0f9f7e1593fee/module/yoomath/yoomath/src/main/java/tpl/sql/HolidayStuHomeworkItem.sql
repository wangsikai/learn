##查询学生寒假作业专项信息
#macro($queryStuHkItems(holidayHomeworkId,userId,holidayStuHomeworkId,stuIds))
SELECT * FROM holiday_stu_homework_item where 1=1
#if(holidayHomeworkId)
and holiday_homework_id =:holidayHomeworkId
#end
#if(holidayStuHomeworkId)
and holiday_stu_homework_id =:holidayStuHomeworkId
#end
#if(userId)
and student_id =:userId
#end
#if(stuIds)
and student_id in (:stuIds)
#end
#end

##根据作业专项ID 获取所有学生作业
#macro($findStudentHomework(homeworkItemId))
SELECT * FROM holiday_stu_homework_item WHERE holiday_homework_item_id = :homeworkItemId
#end

## 查找学生作业下所有已经统计出来的专项作业
#macro($findItemByStuHk(stuHkId))
SELECT t.* FROM holiday_stu_homework_item t WHERE t.holiday_stu_homework_id = :stuHkId
#end

## 根据假期作业专项查找所有的学生专项列表
#macro($findByHkItem(hkItemId))
SELECT t.* FROM holiday_stu_homework_item t WHERE t.holiday_homework_item_id = :hkItemId
#end

## 查找已经批改完成但还未计算数据的学生专项id
#macro($queryNotCalculate())
SELECT t.*
FROM holiday_stu_homework_item t
WHERE t.id < :next AND t.del_status = 0 AND t.status in (1, 2) AND t.completion_rate is not null and t.right_rate IS NULL
#end

## 查找学生专项按照正确率倒序并正确率不为空
#macro($findByHkItemRightRate(hkItemId))
SELECT t.* FROM holiday_stu_homework_item t WHERE t.holiday_homework_item_id = :hkItemId
AND t.right_rate IS NOT NULL ORDER BY t.right_rate DESC
#end


##更新学生作业项表状态
#macro($updateStuHolidayHomeworkItem(homeworkId))
UPDATE holiday_stu_homework_item  SET del_status = 1 where holiday_homework_id = :homeworkId
#end

##获取学生和班级专项的正确率统计
#macro($getClazzStat(holidayStuHomeworkId))
SELECT a.right_rate class_right_rate,b.right_rate stu_right_rate FROM holiday_homework_item a 
INNER JOIN holiday_stu_homework_item b ON a.id = b.holiday_homework_item_id 
AND b.holiday_stu_homework_id = :holidayStuHomeworkId
#end

## 更新学生专项状态
#macro($updateStatus(hdHkId,status))
UPDATE holiday_stu_homework_item SET status = :status WHERE holiday_homework_id = :hdHkId
#end

## 获取学生作业下各个专项的总完成
#macro($getSumComplete(holidayStuHomeworkId))
SELECT SUM(completion_rate) FROM  holiday_stu_homework_item WHERE holiday_stu_homework_id =:holidayStuHomeworkId AND completion_rate IS NOT NULL
#end

##通过假期作业ID获取没有提交作业的学生专项ID 的集合
#macro($queryStuItemIds(holidayHomeworkId,status))
SELECT * FROM holiday_stu_homework_item WHERE holiday_homework_id = :holidayHomeworkId AND status =:status AND del_status = 0
#end

##通过寒假作业专项ID和学生ID获取学生寒假作业专项
#macro($find(holidayHomeworkItemId,studentId))
SELECT * FROM holiday_stu_homework_item WHERE holiday_homework_item_id = :holidayHomeworkItemId AND student_id = :studentId
#end

##删除全部学生作业
#macro($deleteStuHolidayHomeworkItemByClazz(holidayids))
UPDATE holiday_stu_homework_item  SET del_status = 2
where holiday_homework_id IN (:holidayids)
and del_status != 1
#end