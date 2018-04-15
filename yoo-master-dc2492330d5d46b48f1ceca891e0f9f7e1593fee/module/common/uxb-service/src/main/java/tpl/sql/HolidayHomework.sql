##作业转让,没有下发的作业更新创建人
#macro($holidayHkTransfer(classId,newTeacherId))
	update holiday_homework set create_id = :newTeacherId where homework_class_id = :classId and status != 3
#end