## 根据UserTask.code及用户id查找数据
#macro($findByCodeAndUser(code,userId,date))
SELECT t.*
FROM user_task_log t INNER JOIN user_task u ON u.code = t.task_code
WHERE t.task_code = :code AND t.user_id = :userId AND u.status = 2 AND t.status < 3
#if(date)
AND t.create_at = :date
#end
#end

## 根据UserTask.code列表以及用户id查找数据
#macro($findByCodesAndUser(codes,userId))
SELECT t.*
FROM user_task_log t INNER JOIN user_task u ON u.code = t.task_code
WHERE t.task_code IN :codes AND t.user_id = :userId AND u.status = 2 AND t.status < 3
order by t.sequence ASC
#end

## 查询UserTaskLog数据
#macro($query(codes,userId))
SELECT l.* FROM user_task_log l
INNER JOIN(
SELECT t.task_code AS code,MAX(t.create_at) AS ct FROM user_task_log t
WHERE t.user_id=:userId AND t.status < 3
GROUP BY t.task_code
) tb ON tb.code=l.task_code AND tb.ct=l.create_at AND l.user_id=:userId
where l.task_code IN (:codes) AND l.status < 3
#end

## 统计UserTaskLog数据
#macro($countLogByCode(code,userId))
select count(1) from user_task_log where  user_id=:userId and  task_code =:code AND status < 3
#end

## 根据任务类型统计未领取的任务数量
#macro($countNotReceiveTask(type,userId))
SELECT count(t.id) FROM user_task_log t INNER JOIN user_task ut ON ut.code = t.task_code
WHERE ut.type = :type AND ut.status = 2 AND t.user_id = :userId AND t.status = 1
#end

## 查找用户最近一次完成任务时间
#macro($getLatestCompleteDate(type,userId))
SELECT t.* FROM user_task_log t INNER JOIN user_task ut ON ut.code = t.task_code
WHERE ut.type = :type AND t.user_id = :userId AND t.status < 3 AND ut.status = 2 ORDER BY t.complete_at DESC LIMIT 1
#end