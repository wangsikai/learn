##获取题库学校
#macro($taskFindAll(status))
select * from  question_school where status =:status
#end


##统计学校老师校级会员数量
#macro($taskGetStaticSchoolSvip(schoolId))
select count(1) from  teacher t INNER JOIN user_member um on um.user_id=t.id  
where school_id=:schoolId and um.member_type=2
#end
