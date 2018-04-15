##活动学生排名
#macro($queryActivityStudentRank(activityCode,accountName,realName,channelName,schoolName,tag,type))
select iears.id,hc.code,hc.name as clazzName,ae.name as teacherAccountName,u.name as realName,a.name as accountName,t.phase_code,uc.name as channelName,
sl.name as schoolName,hc.student_num,iears.score,iears.manual_score,iears.tag,iears.submit_at,iears.do_time
from imperial_exam_activity_rank_student iears 
INNER JOIN imperial_exam_activity_student ieau ON iears.user_id=ieau.user_id and  ieau.activity_code=:activityCode and iears.activity_code=:activityCode
INNER JOIN user u ON u.id =iears.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN student t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
INNER JOIN imperial_exam_homework_student ieh on ieh.id=iears.activity_homework_id
INNER JOIN homework_student_class hsc on hsc.student_id =iears.user_id and ieh.clazz_id = hsc.class_id 
INNER JOIN homework_class hc on hsc.class_id =hc.id
INNER JOIN teacher te on hc.teacher_id =te.id
INNER JOIN user ue ON ue.id =te.id and u.status=0
INNER JOIN account ae on  ae.id=ue.account_id
where iears.activity_code =:activityCode and iears.type=:type
#if(accountName)
and a.name like :accountName
#end
#if(channelName)
and uc.name like :channelName
#end 
#if(schoolName)
 and sl.name like :schoolName
#end
#if(realName)
 and u.name like:realName
#end
#if(tag)
 and iears.tag =:tag
#end
order by iears.score DESC,iears.do_time ASC,iears.submit_at ASC
#end

##活动老师排名
#macro($queryActivityTeacherRank(activityCode,accountName,channelName,schoolName,grade,clazzCode,tag,room,type))
select iear.id,hc.code,hc.name as clazzName,ieau.grade,a.name as accountName,t.phase_code,hk.right_rate,ieau.name,uc.name as channelName,
sl.name as schoolName,hc.student_num,iear.score,iear.manual_score,iear.tag,
round(hk.commit_count*1000/hk.distribute_count,1)/10 submitRate,iear.do_time
from imperial_exam_activity_rank iear 
INNER JOIN imperial_exam_activity_user ieau ON iear.user_id=ieau.user_id and  ieau.activity_code=:activityCode and  iear.activity_code=:activityCode
INNER JOIN user u ON u.id =iear.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN teacher t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
INNER JOIN imperial_exam_homework ieh on ieh.id=iear.activity_homework_id
INNER JOIN homework hk ON ieh.homework_id = hk.id
INNER JOIN homework_class hc on hk.homework_class_id =hc.id
where ieau.activity_code =:activityCode and iear.type=:type
#if(accountName)
and a.name like :accountName
#end
#if(channelName)
and uc.name like :channelName
#end 
#if(schoolName)
 and sl.name like :schoolName
#end
#if(grade)
 and ieau.grade =:grade
#end
#if(clazzCode)
 and hc.code =:clazzCode
#end
#if(tag)
 and iear.tag =:tag
#end
#if(room)
 and iear.room =:room
#end
order by iear.manual_score desc,iear.do_time ASC
#end

##更新分数
#macro($updateActivityRank(rankId,score))
update imperial_exam_activity_rank set manual_score =:score where  id =:rankId
#end

##获取排行对象
#macro($updateScoreAndTime(id,type))
select t.* from imperial_exam_activity_rank t where t.type=:type and t.id=id
#end

