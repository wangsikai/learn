## 查找最新一期数据
#macro($findLatest())
SELECT t.* FROM coins_lo_season t
ORDER BY t.id DESC LIMIT 1
#end

## 查找所有活动最新一期数据(一或多个)
#macro($findNewList())
SELECT * FROM coins_lo_season WHERE id IN 
(
	SELECT MAX(id) FROM coins_lo_season WHERE STATUS != 2 and type =0 GROUP BY CODE
)
#end