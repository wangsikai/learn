#macro($findOverdueAccount(beforeDate))
SELECT * FROM account 
WHERE status = 1 AND register_at < :beforeDate AND id < :next ORDER BY id DESC
#end

#macro($find6DayAccount(beginDate,endData))
SELECT * FROM account 
WHERE status = 1 AND register_at > :beginDate AND register_at < :endData AND id < :next ORDER BY id DESC
#end

##用户列表
#macro($findUserList(schoolId,userNameStr,mobile,createId))
SELECT a.id FROM USER a 
INNER JOIN account b ON a.account_id = b.id
INNER JOIN teacher c ON a.id = c.id
WHERE c.school_id = :schoolId and a.id !=:createId and a.user_channel_code is not null and a.user_channel_code > 10000
#if(userNameStr)
 	and a.name like :userNameStr
#end
#if(mobile)
 	and b.mobile = :mobile
#end
#end