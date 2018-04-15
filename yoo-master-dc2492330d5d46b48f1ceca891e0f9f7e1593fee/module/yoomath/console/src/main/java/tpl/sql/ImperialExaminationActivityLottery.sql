##最终中奖老师
#macro($queryActivityLotteryTeacher(activityCode,accountName,realName,schoolName,process,status,room))
select ieal.id,ieal.lottery_at as lotteryAt,ieal.process,a.name as accountName,u.name as realName,t.phase_code,uc.name as channelName,
sl.name as schoolName,ieal.name,ieal.status,ieau.room,ieal.received
from imperial_exam_activity_lottery ieal
INNER JOIN imperial_exam_activity_user ieau ON ieal.user_id=ieau.user_id and ieau.activity_code=:activityCode
INNER JOIN user u ON u.id =ieal.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT  JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN teacher t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
where ieal.activity_code =:activityCode and ieal.prizes_id is not null and ieal.prizes_id !=0
#if(accountName)
and a.name like :accountName
#end
#if(realName)
and u.name like :realName
#end 
#if(schoolName)
 and sl.name like:schoolName
#end
#if(process)
 and ieal.process =:process
#end
#if(status)
 and ieal.received =:status
#end
#if(room)
 and ieau.room =:room
#end
order by ieal.lottery_at desc
#end

##最终中奖学生
#macro($queryActivityLotteryStudent(activityCode,accountName,realName,schoolName,process,status))
select ieal.id,ieal.lottery_at as lotteryAt,ieal.process,a.name as accountName,u.name as realName,t.phase_code,uc.name as channelName,
sl.name as schoolName,ieal.name,ieal.status,ieal.received
from imperial_exam_activity_lottery ieal
INNER JOIN imperial_exam_activity_student ieau ON ieal.user_id=ieau.user_id and ieau.activity_code=:activityCode
INNER JOIN user u ON u.id =ieal.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT  JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN student t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
where ieal.activity_code =:activityCode and ieal.prizes_id is not null and ieal.prizes_id !=0
#if(accountName)
and a.name like :accountName
#end
#if(realName)
and u.name like :realName
#end 
#if(schoolName)
 and sl.name like:schoolName
#end
#if(process)
 and ieal.process =:process
#end
#if(status)
 and ieal.received =:status
#end
order by ieal.lottery_at desc
#end


##更新领奖状态
#macro($updateLotteryStatus(id,received))
update imperial_exam_activity_lottery set received =:received where  id =:id
#end


