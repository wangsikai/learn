##批量获取作业统计
#macro($mgetStat(hkClazzIds))
select * from homework_stat where homework_class_id in (:hkClazzIds)
#end

##homeworkstat转让
#macro($zycHomeworkStatTransfer(classId,newTeacherId))
	update homework_stat set user_id = :newTeacherId where homework_class_id = :classId
#end