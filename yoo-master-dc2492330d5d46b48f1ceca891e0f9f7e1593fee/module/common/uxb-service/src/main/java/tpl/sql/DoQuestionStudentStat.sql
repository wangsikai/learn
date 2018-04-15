##获取最新的30天的统计
#macro($getLast30Stat(studentId,startTime,endTime))
SELECT count(id) FROM student_fallible_question 
WHERE student_id =:studentId and status=0 and mistake_num > 0
and update_at >:startTime and update_at <:endTime
#end

##获取6个月的统计
#macro($queryLast6MonthStat(studentId))
SELECT do_count,month0,right_count,right_rate FROM do_question_stu_natural_month_stat WHERE student_id =:studentId order by month0 desc
#end

##获取易错知识点
#macro($queryWeakKpList(studentId))
SELECT a.right_rate,a.right_count,a.do_count,b.name, b.code FROM do_question_stu_kp_stat a INNER JOIN knowledge_point b
ON a.knowpoint_code = b.code
WHERE student_id = :studentId AND (right_count+1)/(do_count+2) <= 0.6
ORDER BY (right_count+1)/(do_count+2) ASC 
#end