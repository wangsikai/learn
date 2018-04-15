#macro($findByToken(token,product))
SELECT * FROM device WHERE token = :token AND product = :product
#end

#macro($findByUserId(userId,product))
SELECT * FROM device WHERE user_id = :userId AND product = :product
#end

#macro($findTokenByUserIds(userIds,product))
SELECT token FROM device WHERE user_id IN (:userIds) AND product = :product AND token IS NOT NULL
#end