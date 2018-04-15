##根据假期作业ID 获取所有专项作业列表
#macro($listHdItemById(holidayHomeworkId))
SELECT * FROM holiday_homework_item WHERE holiday_homework_id =:holidayHomeworkId
#end

## 更新分发数量
#macro($updateDistributeCount(id,size))
UPDATE holiday_homework_item SET distribute_count = :size WHERE id = :id
#end

## 根据假期作业查找其下的专项列表
#macro($findByHk(hkId))
SELECT * FROM holiday_homework_item t WHERE t.holiday_homework_id = :hkId
#end

## 批量更新作业专项状态
#macro($updateStatus(ids,status))
UPDATE holiday_homework_item SET status = :status WHERE id IN :ids
#end

##更新假期作业项状态，已下发不能删除
#macro($updateHolidayHomeworkItem(homeworkId,teacherId))
UPDATE holiday_homework_item SET del_status = 1 WHERE holiday_homework_id = :homeworkId AND create_id = :teacherId and status != 3
#end

## 更新为完成状态
#macro($updateStatusByHomework(hdHkId,status))
UPDATE holiday_homework_item SET status = :status WHERE holiday_homework_id = :hdHkId
#end

##删除全部假期作业数据
#macro($deleteHolidayHomeworkItemByClazz(homeworkClassId))
UPDATE holiday_homework_item SET del_status = 2 WHERE homework_class_id = :homeworkClassId
and del_status != 1
#end