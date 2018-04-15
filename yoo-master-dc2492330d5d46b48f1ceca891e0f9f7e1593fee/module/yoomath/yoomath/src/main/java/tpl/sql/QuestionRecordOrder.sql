## 根据用户查询录题请求记录
#macro($findByUser(userId))
SELECT t.* FROM question_record_order t WHERE t.del_status = 0 AND t.user_id = :userId ORDER BY order_at DESC
#end