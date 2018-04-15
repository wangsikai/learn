##查询不同教材的统计
#macro($queryListByTextbookCode(classId,studentId,textbookCode))
SELECT * FROM diagno_stu_class where class_id =:classId and student_id = :studentId and textbook_code =:textbookCode
#end

##查询教材不同题目的班级平均练习数和平均正确数
#macro($getClassAvgDoQuestion(classId,textbookCode))
SELECT ROUND(AVG(do_hard1_count),1) do_hard1_avg,ROUND(AVG(do_hard2_count),1) do_hard2_avg,ROUND(AVG(do_hard3_count),1) do_hard3_avg,
	   ROUND(AVG(right_hard1_count),1) right_hard1_avg,ROUND(AVG(right_hard2_count),1) right_hard2_avg,ROUND(AVG(right_hard3_count),1) right_hard3_avg
FROM diagno_stu_class where class_id =:classId and textbook_code =:textbookCode
#end
