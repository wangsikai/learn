##获取学生id集合
#macro($findStudentIdsByClassId(classId))
	select student_id from homework_student_class where class_id = :classId and status = 0
#end
