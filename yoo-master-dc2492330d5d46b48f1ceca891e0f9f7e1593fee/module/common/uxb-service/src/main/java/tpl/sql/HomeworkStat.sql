##homeworkstat转让
#macro($homeworkStatTransfer(classId,newTeacherId))
	update homework_stat set user_id = :newTeacherId where homework_class_id = :classId
#end