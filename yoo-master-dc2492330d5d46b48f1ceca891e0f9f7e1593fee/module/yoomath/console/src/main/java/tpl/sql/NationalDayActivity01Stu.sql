##国庆活动统计学生排名
#macro($queryNdaStuRank(schoolName,accountName,realName,channelName,phase))
select ndas.user_id as userId,sl.name as schoolName,a.name as accountName,u.name as realName,uc.name as channelName,
s.phase_code as phasecode,ndas.right_count as rightCount, a.mobile
from national_day_activity_01_stu ndas
INNER JOIN user u ON u.id =ndas.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT  JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN student s ON s.id =u.id 
LEFT JOIN school sl on s.school_id =sl.id
where  1 = 1
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
and u.name like :realName
#end
#if(phase)
and s.phase_code=:phase
#end
order by ndas.right_count DESC,ndas.user_id ASC
#end