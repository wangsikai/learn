##查询pk记录
#macro($queryRedoresByForm(activityCode, startAt, endAt, accountName, realName, 
schoolName, realMan))
select pk.* from holiday_activity_02_pkrecord pk
INNER JOIN user u ON u.id =pk.user_id and u.status=0
INNER JOIN account a on a.id=u.account_id
INNER JOIN student s ON s.id =u.id 
LEFT JOIN school sl on s.school_id =sl.id
INNER JOIN holiday_activity_02_question qu on pk.id = qu.pk_record_id
INNER JOIN holiday_activity_02_answer an on qu.id = an.questions_id
where pk.activity_code = :activityCode
#if(startAt)
and pk.pk_at >= :startAt and pk.pk_at <= :endAt
#end
#if(accountName)
and a.name like :accountName
#end
#if(realName)
and u.name like :realName
#end
#if(schoolName)
and sl.name like :schoolName
#end
#if(realMan)
and pk.real_man = :realMan
#end
order by pk.pk_at DESC
#end

##根据pkId查询用户信息
#macro($queryUserInfoById(pkId))
select pk.id as id, 
a.name as accountName, 
u.name as realName, 
sl.name as schoolName,
an.right_rate as rightRate
from holiday_activity_02_pkrecord pk
INNER JOIN user u ON u.id =pk.user_id and u.status=0
INNER JOIN account a on a.id=u.account_id
INNER JOIN student s ON s.id =u.id 
LEFT JOIN school sl on s.school_id =sl.id
INNER JOIN holiday_activity_02_question qu on pk.id = qu.pk_record_id
LEFT JOIN holiday_activity_02_answer an on qu.id = an.questions_id
where pk.id = :pkId
#end