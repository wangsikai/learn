## 查询当前实际个数(旧知识点个数)
#macro($indexDataCount())
SELECT SUM(t.COUNT) FROM (
	SELECT COUNT(1) COUNT FROM meta_knowpoint  where status = 0
		UNION ALL 
	SELECT COUNT(1) COUNT FROM knowpoint where status = 0
	) t
#end
