## 按需查询用户任务列表
#macro($query(type,userType,scope,status))
SELECT * FROM user_task t
WHERE t.type <= 2
#if(type)
AND t.type = :type
#end
#if(userType)
AND t.user_type = :userType
#end
#if(scope)
#if(scope==1)
AND t.user_scope <= 1
#end
#if(scope==2)
AND t.user_scope IN (0, 2)
#end
#end
#if(status)
AND t.status = :status
#end
ORDER BY t.type ASC,t.sequence ASC
#end

## 查找用户未完成的每日任务以及新手任务
#macro($queryUserNotFinishTask(userType,scope,status,date,userId))
SELECT t.*
FROM user_task t
WHERE t.code NOT IN (
  SELECT DISTINCT u.task_code
  FROM user_task_log u
  WHERE u.create_at = :date AND u.task_type = 1 AND u.status = 2 AND u.user_id = :userId
  UNION ALL
  SELECT DISTINCT u.task_code
  FROM user_task_log u
  WHERE u.task_type = 0 AND u.status = 2 AND u.user_id = :userId
)
AND t.type <= 1
#if(userType)
AND t.user_type = :userType
#end
#if(scope)
#if(scope==1)
AND t.user_scope <= 1
#end
#if(scope==2)
AND t.user_scope IN (0, 2)
#end
#end
#if(status)
AND t.status = :status
#end
ORDER BY t.type ASC, t.sequence ASC
#end