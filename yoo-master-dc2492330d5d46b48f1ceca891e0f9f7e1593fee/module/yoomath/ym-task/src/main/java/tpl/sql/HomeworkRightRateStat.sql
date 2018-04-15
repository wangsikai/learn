## 获取一周平均正确率统计
#macro($findWeekRateList(classId))
	SELECT ROUND(AVG(stat.right_rate)) avg_rate FROM student_homework_stat stat
INNER JOIN homework_student_class sc ON sc.class_id=stat.homework_class_id AND sc.student_id=stat.user_id AND sc.status=0
WHERE stat.homework_class_id=:classId
#end