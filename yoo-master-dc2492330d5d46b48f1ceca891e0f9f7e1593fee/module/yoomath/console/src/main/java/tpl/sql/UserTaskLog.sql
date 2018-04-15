## Disable所有任务日志数据
#macro($zycDisabled(taskCode))
UPDATE user_task_log SET status = 3 WHERE task_code = :taskCode
#end

## 根据任务code以及用户id查找数据
#macro($findByUserTaskAndUser(taskCode,userId))
SELECT t.* FROM user_task_log t WHERE t.task_code = :taskCode AND t.user_id = :userId
#end

## 根据类型启用相关任务
#macro($zycEnableByType(type))
UPDATE user_task_log SET status = 0 WHERE task_type = :type
#end

## 根据用户id列表及任务code查询数据
#macro($zycFindByCodeAndUsers(userIds,code))
SELECT t.* FROM user_task_log t WHERE t.user_id IN :userIds AND t.task_code = :code
#end

#macro($zycQuery(taskCode))
SELECT t.* FROM user_task_log t WHERE t.task_code = :taskCode AND t.id < :next ORDER BY t.id DESC
#end

## 根据id列表disabled掉数据
#macro($zycDisabledByIds(ids))
UPDATE user_task_log SET status = 3 WHERE id IN :ids
#end