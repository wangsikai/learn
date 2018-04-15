## 查找最新一条数据
#macro($zyPullNewestOne(userId))
SELECT t.* FROM yoomath_customer_service_log t WHERE t.user_id = :userId AND t.status = 0 ORDER BY t.id DESC LIMIT 1
#end

## 游标查询信息
#macro($zyPull(userId,status))
SELECT t.* FROM yoomath_customer_service_log t
WHERE t.user_id = :userId
#if(status == 0)
AND t.id > :next
#end
#if(status == 1)
AND t.id < :next
#end
#if(status)
AND t.user_read_status = :status
#end
#if(status == 0)
ORDER by t.id ASC
#end
#if(status == 1)
ORDER by t.id DESC
#end
#end

#macro($zyUpdateReadStatus(ids))
UPDATE yoomath_customer_service_log SET user_read_status = 1 WHERE id IN :ids
#end