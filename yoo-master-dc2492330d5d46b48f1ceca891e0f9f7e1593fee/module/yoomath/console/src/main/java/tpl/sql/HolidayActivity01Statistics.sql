##活动统计排名
#macro($queryActivityStatisticsRank(activityCode,accountName,channelName,schoolName,
clazzName,realName,phase,startPeriodTime,isAll))
select hast.id,hc.name as clazzName,sl.name as schoolName,a.name as accountName,u.name as name,uc.name as channelName,
t.phase_code,hc.student_num,hast.submit_rate,hast.right_rate,hast.score,hast.period_score,hast.homework_count
from holiday_activity_01_statistics hast
INNER JOIN user u ON u.id =hast.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT  JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN teacher t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
INNER JOIN homework_class hc on hast.class_id =hc.id
where  hast.activity_code=:activityCode
#if(accountName)
and a.name like :accountName
#end
#if(channelName)
and uc.name like :channelName
#end
#if(schoolName)
and sl.name like :schoolName
#end
#if(clazzName)
and hc.name like :clazzName
#end
#if(realName)
and u.name like :realName
#end
#if(phase)
and t.phase_code=:phase
#end
#if(startPeriodTime)
and hast.start_period_time =:startPeriodTime
#end
#if(endPeriodTime)
and hast.end_period_time =:endPeriodTime
#end
#if(isAll==false)
order by hast.period_score DESC,hast.right_rate DESC
#elseif(isAll==true)
order by hast.score DESC,hast.right_rate DESC
#end
#end