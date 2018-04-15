## 前一天知识点推荐查询
#macro($taskGetKnowpointCard(students))
select  sh.student_id,kpc.id as cardid from student_homework sh 
INNER JOIN homework_knowledge hk on sh.homework_id=hk.homework_id INNER JOIN knowledge_point kp
on kp.code =hk.knowledge_code and kp.status=0 INNER JOIN knowledge_point_card kpc on kpc.knowpoint_code=kp.code and kpc.del_status=0
and check_status=2 
INNER JOIN homework h on sh.homework_id=h.id and h.del_status=0 INNER JOIN homework_class hc on h.homework_class_id=hc.id and hc.status=0 INNER JOIN 
homework_student_class hsc on hsc.class_id=hc.id and hsc.status=0 and hsc.student_id =sh.student_id
where sh.student_id in :students and sh.del_status=0 and sh.status =0 
GROUP BY kp.code,sh.student_id,kpc.id  
order by count(kp.code) DESC,kp.difficulty DESC,sh.create_at ASC
#end

## 知识点推荐查询
#macro($taskGetKnowpointCardByStatus(students))
select * from recommend_knowpoint_card where status=0 and user_id in :students
#end

## 查询作业中学生id
#macro($taskGetStudentIds())
select distinct student_id as id  from student_homework 
#end