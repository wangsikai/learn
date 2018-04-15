## 根据日期以及用户id查询数据
#macro($query(date,userId))
SELECT t.* FROM user_task_star_log t WHERE t.user_id = :userId AND t.create_at = :date
#end