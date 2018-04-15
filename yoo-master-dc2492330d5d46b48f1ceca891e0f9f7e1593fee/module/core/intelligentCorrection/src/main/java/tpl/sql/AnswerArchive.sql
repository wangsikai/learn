## 查询归档的答案
#macro($find(answerId,content))
SELECT * FROM answer_archive WHERE answer_id = :answerId
#if(content)
AND content = :content
#end
#end

## 删除归档的答案
#macro($delete(answerId,content,result))
DELETE FROM answer_archive WHERE answer_id = :answerId
#if(content)
AND content = :content
#end
#if(result)
AND result = :result
#end
#end