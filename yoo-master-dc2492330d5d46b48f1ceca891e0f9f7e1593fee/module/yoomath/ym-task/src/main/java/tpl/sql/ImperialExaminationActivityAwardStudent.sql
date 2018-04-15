## 综合排名
#macro($TaskStudentAwardRank(code))
SELECT * FROM imperial_exam_activity_award_student 
where activity_code =:code and status =0 GROUP BY user_id ORDER BY score desc,do_time asc
#end