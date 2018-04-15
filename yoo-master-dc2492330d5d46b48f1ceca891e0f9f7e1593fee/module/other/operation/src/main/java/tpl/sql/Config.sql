## 查询
#macro($opFind(key0,key0s))
SELECT * FROM config WHERE 1 = 1
#if(key0)
	AND key0 = :key0
#end
#if(key0s)
	AND key0 IN (:key0s)
#end
#end