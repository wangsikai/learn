#macro($taskGetStuClassTextbook(clazzId,studentId,textbookCode))
SELECT * FROM diagno_stu_class_textbook WHERE class_id = :clazzId AND student_id = :studentId and textbook_code =:textbookCode
#end
