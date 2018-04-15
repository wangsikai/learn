##根据用户查询rank记录
#macro($queryByUser(activityCode, userId))
select * from holiday_activity_02_power_rank 
where activity_code = :activityCode
and user_id = :userId
#end

##查询战力记录
#macro($queryPowerRankList(activityCode))
select prank.id as id,
prank.rank as rank,
prank.power as power,
um.member_type as memberType,
a.name as accountName,
a.mobile,
u.name as realName,
sl.name as schoolName,
s.enter_year as enterYear,
uc.name as channelName
from holiday_activity_02_power_rank prank
INNER JOIN user u ON u.id = prank.user_id and u.status=0
INNER JOIN account a on a.id=u.account_id
INNER JOIN student s ON s.id =u.id 
LEFT JOIN user_channel uc ON u.user_channel_code =uc.code
LEFT JOIN school sl on s.school_id =sl.id
LEFT JOIN user_member um on um.user_id = u.id
where prank.activity_code = :activityCode
order by prank.rank ASC
#end