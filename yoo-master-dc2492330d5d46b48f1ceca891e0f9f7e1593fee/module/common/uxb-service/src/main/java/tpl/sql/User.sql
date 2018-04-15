#macro($getUserByAccountId(accountId))
SELECT * FROM user WHERE account_id = :accountId
#end

#macro($queryUser(excludeUserIds,name))
SELECT * FROM user WHERE 1=1
#if(excludeUserIds)
	AND id NOT IN :excludeUserIds
#end
#if(name)
	AND name LIKE :name
#end
	AND id < :next ORDER BY id DESC
#end

##分页获取user
#macro($getAllUserByPage())
SELECT * FROM user WHERE id < :next ORDER BY id DESC
#end