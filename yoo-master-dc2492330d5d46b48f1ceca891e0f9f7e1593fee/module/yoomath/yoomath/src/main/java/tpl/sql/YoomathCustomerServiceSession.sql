## 查找会话
#macro($zyFindSession(userId,customerId))
SELECT * FROM yoomath_customer_service_session
WHERE user_id = :userId AND customer_service_id = :customerId
#end