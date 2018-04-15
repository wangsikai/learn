##获取使用过APP的未报名初中教师用户的设备
#macro($getDeviceFromNotSignUpTeacher())
SELECT t.* FROM device t
 INNER JOIN USER u ON u.id=t.user_id AND u.user_type=1
 LEFT JOIN imperial_exam_activity_user au ON au.user_id=t.user_id
 INNER JOIN teacher th ON th.id=u.id AND th.phase_code=2
 WHERE au.id IS NULL
#end

##获取已报名初中教师用户的设备
#macro($getDeviceFromSignUpTeacher(code))
SELECT t.* FROM device t
 INNER JOIN USER u ON u.id=t.user_id AND u.user_type=1
 INNER JOIN imperial_exam_activity_user au ON au.user_id=t.user_id
 	#if(code)
		AND au.activity_code = :code
	#end
 INNER JOIN teacher th ON th.id=u.id AND th.phase_code=2
#end

##获取所有初中教师用户的设备
#macro($getDeviceFromTeacher())
SELECT t.* FROM device t
 INNER JOIN USER u ON u.id=t.user_id AND u.user_type=1
 INNER JOIN teacher th ON th.id=u.id AND th.phase_code=2
#end

##获取所有初中学生用户的设备
#macro($getDeviceFromStudent())
SELECT t.* FROM device t
 INNER JOIN USER u ON u.id=t.user_id AND u.user_type=2
 INNER JOIN student th ON th.id=u.id AND th.phase_code=2
#end

##获取未提交作业初中学生用户的设备
#macro($getDeviceFromNotCommitStudent(code,type))
SELECT t.* FROM device t
 INNER JOIN imperial_exam_activity_student at ON at.user_id=t.user_id  AND at.activity_code=:code
 INNER JOIN USER u ON u.id=t.user_id AND u.user_type=2
 INNER JOIN student th ON th.id=u.id AND th.phase_code=2 
 INNER JOIN imperial_exam_homework_student iehs on iehs.user_id=at.user_id AND iehs.type = :type
 INNER JOIN student_homework shk ON iehs.homework_id = shk.homework_id AND shk.student_id = at.user_id AND shk.status = 0
#end