#macro($deleteByClassId(classId))
delete from homework_student_clazz_stat where class_id = :classId
#end

#macro($getAvgData(classId, beginDate, endDate))
select avg(s.right_rate) as avgRight, s.student_id as studentId from student_homework s
inner join homework t on t.id = s.homework_id
where t.homework_class_id = :classId
and t.status = 3
and t.create_at >= :beginDate and t.create_at <= :endDate and s.submit_at is not null and s.stu_submit_at is not null
group by student_id
#end