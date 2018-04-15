## 查找题目类型
#macro($listAll(status))
SELECT * FROM question_category
#if(status)
 where status=:status
#end
ORDER BY code ASC
#end