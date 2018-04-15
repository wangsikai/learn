##最终老师奖项排名
#macro($queryActivityTeacherAward(activityCode,accountName,channelName,schoolName,awardLevel,status,room))
select ieaa.id,IFNULL(ieaa.rank,0) rank,a.name as accountName,u.name as realName,t.phase_code,uc.name as channelName,
sl.name as schoolName,ieau.name,ieau.mobile,hc.name as clazzName,ieaa.score,ieaa.do_time,ieaa.award_level,
ieaa.award_delivery_address as address,ieaa.award_contact as contact,
ieaa.award_contact_number as contactnumber,ieaa.status
from imperial_exam_activity_award ieaa
INNER JOIN imperial_exam_activity_user ieau ON ieaa.user_id=ieau.user_id and ieau.activity_code=:activityCode
INNER JOIN user u ON u.id =ieaa.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT  JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN teacher t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
INNER JOIN homework_class hc on ieaa.clazz_id =hc.id
where ieaa.activity_code =:activityCode
#if(accountName)
and a.name like :accountName
#end
#if(channelName)
and uc.name like :channelName
#end 
#if(schoolName)
 and sl.name like:schoolName
#end
#if(levelType==1)
  and ieaa.award_level is null
#elseif(levelType==2)
  and ieaa.award_level =:awardLevel
#end
#if(status)
 and ieaa.status =:status
#end
#if(room)
 and ieaa.room =:room
#end
order by ieaa.status asc,ieaa.rank asc,ieaa.score desc,ieaa.do_time asc 
#end

##最终学生奖项排名
#macro($queryActivityStudentAward(activityCode,accountName,channelName,schoolName,awardLevel))
select ieaas.id,IFNULL(ieaas.rank,0) rank,ae.name as teacherAccountName,a.name as accountName,u.name as realName,t.phase_code,uc.name as channelName,
sl.name as schoolName,hc.name as clazzName,ieaas.score,ieaas.do_time,ieaas.award_level,
ieaas.award_delivery_address as address,ieaas.award_contact as contact,
ieaas.award_contact_number as contactnumber,ieaas.status
from imperial_exam_activity_award_student ieaas
INNER JOIN imperial_exam_activity_student ieau ON ieaas.user_id=ieau.user_id and ieau.activity_code=:activityCode
INNER JOIN user u ON u.id =ieaas.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT  JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN student t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
INNER JOIN homework_student_class hsc on hsc.student_id =ieaas.user_id and hsc.class_id = ieaas.clazz_id
INNER JOIN homework_class hc on hsc.class_id =hc.id
INNER JOIN teacher te on hc.teacher_id =te.id
INNER JOIN user ue ON ue.id =te.id and u.status=0
INNER JOIN account ae on  ae.id=ue.account_id
where ieaas.activity_code =:activityCode
#if(accountName)
and a.name like :accountName
#end
#if(channelName)
and uc.name like :channelName
#end 
#if(schoolName)
 and sl.name like:schoolName
#end
#if(levelType==1)
  and ieaas.award_level is null
#elseif(levelType==2)
  and ieaas.award_level =:awardLevel
#end
order by ieaas.status asc,ieaas.rank asc,ieaas.score desc,ieaas.do_time asc 
#end

##查询用户综合平均分和用时
#macro($getActivityClazzScore(userId,code))
select  ieh.clazz_id,
t.user_id,
ROUND(SUM(t.manual_score)/3, 0) avg_score,
ROUND(SUM(IFNULL(t.do_time,0))/3, 0) avg_dotime 
FROM imperial_exam_activity_rank t INNER JOIN imperial_exam_homework ieh on t.activity_homework_id = ieh.id 
where t.activity_code=:code
#if(userId)
and t.user_id =:userId
#end
GROUP BY ieh.clazz_id 
ORDER BY avg_score desc,avg_dotime asc
#end

##通过活动code和User查询Award
#macro($queryActivityAwardByUser(userId,code))
select * from imperial_exam_activity_award t  where t.activity_code=:code
#if(userId)
 and t.user_id =:userId
#end
#end



