##国庆活动统计教师排名
#macro($queryNdaTeaRank(schoolName,accountName,realName,channelName,phase))
select ndat.user_id as userId,sl.name as schoolName,a.name as accountName,u.name as realName,uc.name as channelName,
t.phase_code as phasecode,ndat.homework_count as homeworkCount, 
ndat.commit_rate as commitRate, ndat.score, a.mobile
from national_day_activity_01_tea ndat
INNER JOIN user u ON u.id =ndat.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT  JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN teacher t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
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
and t.phase_code=:phase
#end
ORDER BY ndat.score DESC, ndat.homework_count DESC, ndat.commit_rate DESC, ndat.user_id ASC
#end