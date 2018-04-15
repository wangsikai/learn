## 获取指定条数的格言
#macro($zyList(limit))
SELECT * FROM motto WHERE status = 0 LIMIT :limit
#end