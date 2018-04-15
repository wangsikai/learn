##查询学生诊断对应的班级
#macro($queryTextBookList(classId,studentId,sort,categoryCode))
SELECT textbook_code FROM diagno_stu_class_textbook where class_id =:classId and student_id = :studentId
	#if(categoryCode)
		AND textbook_code like :categoryCode
	#end
	#if(sort)
		order by textbook_code
	#end
#end