##查询周战力记录
#macro($queryWeekPowerRankByPhase(activityCode, startTime, endTime))
select wrank.id as id,
wrank.rank as rank,
wrank.week_power as weekPower,
um.member_type as memberType,
a.name as accountName,
a.mobile,
u.name as realName,
sl.name as schoolName,
s.enter_year as enterYear,
uc.name as channelName
from holiday_activity_02_week_power_rank wrank
INNER JOIN user u ON u.id =wrank.user_id and u.status=0
INNER JOIN account a on a.id=u.account_id
INNER JOIN student s ON s.id =u.id 
LEFT JOIN user_channel uc ON u.user_channel_code =uc.code
LEFT JOIN school sl on s.school_id =sl.id
LEFT JOIN user_member um on um.user_id = u.id
where wrank.activity_code = :activityCode
#if(startTime)
and wrank.start_time >= :startTime and wrank.end_time <= :endTime
#end
order by wrank.rank ASC
#end