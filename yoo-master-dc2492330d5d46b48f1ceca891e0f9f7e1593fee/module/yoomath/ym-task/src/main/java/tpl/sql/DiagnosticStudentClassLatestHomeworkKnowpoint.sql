#macro($taskGetStuClassLatestHkKp(studentId,knowpointCode,classId,times))
SELECT * FROM diagno_stu_class_latest_hk_kp 
WHERE student_id = :studentId AND knowpoint_code =:knowpointCode AND class_id =:classId AND times =:times
#end

#macro($taskDelStuClassLastestHkKp(classId,studentId))
DELETE FROM diagno_stu_class_latest_hk_kp WHERE class_id =:classId
#if(studentId)
	and student_id = :studentId
#end
#end

#macro($getStuLastestClassKpCount())
SELECT count(1) FROM diagno_stu_class_latest_hk_kp
#end
