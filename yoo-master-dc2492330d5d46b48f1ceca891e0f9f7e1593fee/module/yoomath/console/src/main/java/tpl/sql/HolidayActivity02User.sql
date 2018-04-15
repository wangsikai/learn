##寒假报名用户
#macro($queryHoliday02User(activityCode, startAt, endAt, memberType, accountName, 
mobile, realName, schoolName, channelName))
select 
hau.create_at as createAt,
hau.activity_user_code as activityUserCode,
um.member_type as memberType,
a.name as accountName,
a.mobile,
u.name as realName,
sl.name as schoolName,
s.enter_year as enterYear,
uc.name as channelName
from holiday_activity_02_user hau 
INNER JOIN user u ON u.id =hau.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN student s ON s.id =u.id 
LEFT JOIN school sl on s.school_id =sl.id
LEFT JOIN user_member um on um.user_id = u.id
where  
hau.activity_code = :activityCode
#if(startAt)
and hau.create_at >= :startAt and hau.create_at <= :endAt
#end
#if(accountName)
and a.name like :accountName
#end
#if(mobile)
and a.mobile = :mobile
#end
#if(realName)
and u.name like :realName
#end
#if(schoolName)
and sl.name like :schoolName
#end
#if(channelName)
and uc.name like :channelName
#end
order by hau.create_at DESC
#end