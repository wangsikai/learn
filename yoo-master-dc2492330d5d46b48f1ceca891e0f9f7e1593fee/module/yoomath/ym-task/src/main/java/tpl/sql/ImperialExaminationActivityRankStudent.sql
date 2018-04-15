## 获取学生的分数
#macro($TaskqueryStudentScores(code,type))
select  ieh.clazz_id,
		 t.user_id,
         IFNULL(t.manual_score,0) score,
	     IFNULL(t.do_time,0) do_time
         FROM imperial_exam_activity_rank_student t INNER JOIN imperial_exam_homework_student ieh on t.activity_homework_id = ieh.id 
         where  t.activity_code=:code
		 #if(type)
			and t.type =:type
		 #end
         ORDER BY score desc,do_time asc
#end

## 获取所有学生的分数
#macro($TaskqueryStudentAllScores(code))
select  t.* FROM imperial_exam_activity_rank_student t where t.activity_code=:code
#end

