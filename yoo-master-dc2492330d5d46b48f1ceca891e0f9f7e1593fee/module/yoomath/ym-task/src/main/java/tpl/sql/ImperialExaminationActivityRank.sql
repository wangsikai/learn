## 获取老师的分数
#macro($TaskqueryTeacherScores(code,room,type))
select  ieh.clazz_id,
		 t.user_id,
         t.manual_score score,
	     IFNULL(t.do_time,0) do_time
         FROM imperial_exam_activity_rank t INNER JOIN imperial_exam_homework ieh on t.activity_homework_id = ieh.id 
         where t.activity_code=:code 
         #if(room)
			and t.room =:room
		 #end
		 #if(type)
			and t.type =:type
		 #end
         ORDER BY score desc,do_time asc
#end

## 获取所有老师的分数
#macro($TaskqueryTeacherAllScores(code,type,room,tag))
select  t.* FROM imperial_exam_activity_rank t where t.activity_code=:code and t.type =:type and t.room =:room and t.tag =:tag
#end

