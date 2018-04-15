#macro($getTopStudent(size,classId))
SELECT * FROM homework_student_clazz_stat t 
INNER JOIN homework_student_class hsc ON hsc.class_id=t.class_id AND hsc.student_id=t.student_id AND hsc.status=0
WHERE t.class_id = :classId AND t.status = 0 ORDER BY t.days30_right_rate DESC limit :size
#end

## 根据班级id及学生id查找统计数据
#macro($getByStudentId(studentId,classId))
SELECT * FROM homework_student_clazz_stat t WHERE t.student_id = :studentId AND t.class_id = :classId
#end

##通过正确率和班级获取学生
#macro($findStudentByRightRate(days30RightRate,classId))
SELECT * FROM homework_student_clazz_stat t 
INNER JOIN homework_student_class hsc ON hsc.class_id=t.class_id AND hsc.student_id=t.student_id AND hsc.status=0
WHERE t.class_id = :classId AND t.days30_right_rate = :days30RightRate AND t.status = 0
#end

