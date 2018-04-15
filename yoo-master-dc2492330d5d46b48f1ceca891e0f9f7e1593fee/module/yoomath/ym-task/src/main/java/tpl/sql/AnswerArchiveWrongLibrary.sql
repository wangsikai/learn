## 分页查询answerid
#macro($taskQueryAnswerId(nextAnswerId,fetchCount))
select 
	DISTINCT(answer_id) 
FROM answer_archive_wrong_library 
WHERE status = 0 AND answer_id < :nextAnswerId ORDER BY answer_id DESC LIMIT :fetchCount
#end

## 查询一个空的答案错误次数排名前10的答案
## @since 小悠快批，2018-3-6，错误次数又2次变为10次
#macro($taskQueryWrongAnswer(answerId))
SELECT 
	*
FROM answer_archive_wrong_library 
WHERE status = 0 AND answer_id = :answerId AND times > 10 ORDER BY times DESC LIMIT 10
#end