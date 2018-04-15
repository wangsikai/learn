#macro($nda01findHomeworkByHomeworkId(homeworkIds,startTime,endTime))
select * from student_homework
where homework_id in (:homeworkIds)
and status  in (1, 2)
and del_status = 0
and submit_at >= :startTime
and submit_at <= :endTime
#end