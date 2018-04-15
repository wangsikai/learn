## 班级人数超过19人的
#macro($findExceed20List())
	SELECT * FROM homework_class where student_num  > 19
#end