## 更新归档错误答案库
#macro($updateTimes(answerId,content))
UPDATE answer_archive_wrong_library SET times = times + 1,status = 0 WHERE answer_id = :answerId AND content = :content
#end

## 删除归档错误答案库
#macro($delete(answerId,content))
UPDATE answer_archive_wrong_library SET status = 1 WHERE answer_id = :answerId AND content = :content
#end