## 游标查询信息
#macro($zycPull(csId,userId,status))
SELECT t.* FROM yoomath_customer_service_log t
WHERE t.user_id = :userId
AND t.customer_service_id = :csId
AND t.id > :next
#if(status)
AND t.customer_read_status = :status
#end
ORDER by t.id ASC
#end

#macro($zycUpdateReadStatus(ids))
UPDATE yoomath_customer_service_log SET customer_read_status = 1 WHERE id IN :ids
#end

## 后台客服得到未读信息数
#macro($zycFindUnreadCount())
SELECT count(*) FROM yoomath_customer_service_log t WHERE t.customer_read_status = 0
#end

## 根据用户得到用户发送的未读消息数
#macro($zycGetUsersunreadCount(userIds))
SELECT count(*) as unreadCount, user_id as userId FROM yoomath_customer_service_log
WHERE user_id IN :userIds AND customer_read_status = 0 GROUP BY user_id
#end

## 得到单个用户发送的未读消息数
#macro($zycGetUserunreadCount(userId))
SELECT count(*) FROM yoomath_customer_service_log
WHERE user_id = :userId AND customer_read_status = 0
#end