## 
#macro($taskGetStuClass(classId,studentId,textbookCode))
SELECT * FROM diagno_stu_class  WHERE class_id = :classId AND student_id = :studentId and textbook_code = :textbookCode
#end


##
#macro($taskDelStuClass(classId,studentId))
DELETE FROM diagno_stu_class  WHERE class_id = :classId 
	#if(studentId)
		and student_id = :studentId
	#end
#end