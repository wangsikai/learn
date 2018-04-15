##作业转让,没有下发的作业更新创建人
#macro($zycHolidayItemTransfer(classId,newTeacherId))
	update holiday_homework_item set create_id = :newTeacherId where homework_class_id = :classId and status != 3
#end