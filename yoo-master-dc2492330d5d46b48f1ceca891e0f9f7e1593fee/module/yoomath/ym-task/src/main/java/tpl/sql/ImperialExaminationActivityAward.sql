## 综合排名,分考场
#macro($TaskAwardRank(code,room))
SELECT * FROM imperial_exam_activity_award 
where activity_code =:code and status =0
#if(room)
and room =:room
#end
GROUP BY user_id ORDER BY score desc,do_time asc
#end