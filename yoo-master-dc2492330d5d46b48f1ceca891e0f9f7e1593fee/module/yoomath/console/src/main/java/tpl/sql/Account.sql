#macro($setAccForbidden(accountId))
UPDATE account SET status=1 WHERE id=:accountId
#end

#macro($zycExistsAccount(accountName))
SELECT * FROM account t where t.name = :accountName
#end

#macro($zycGetByUserId(userId))
SELECT t.* FROM account t INNER JOIN user u ON u.account_id = t.id
WHERE u.id = :userId
#end

#macro($zycGetByUserIds(userIds))
SELECT t.mobile, t.email, u.id, t.name FROM account t INNER JOIN user u ON u.account_id = t.id
WHERE u.id IN :userIds
#end

##用户列表
#macro($zycFindUser(accountName))
SELECT a.id,a.name username,b.name accountname,b.mobile,a.user_type,a.user_channel_code FROM USER a 
INNER JOIN account b ON a.account_id = b.id
#if(accountName)
 	and b.name = :accountName
#end
#end