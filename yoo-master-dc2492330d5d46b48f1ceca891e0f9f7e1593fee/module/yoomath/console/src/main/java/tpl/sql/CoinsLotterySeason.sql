##期别数量
#macro($countSeason(code))
SELECT COUNT(id) FROM  coins_lo_season where code =:code
#end

##通过期别名称查询对象
#macro($getByTitleName(title,code))
SELECT * FROM  coins_lo_season where title =:title and code =:code
#end

##获取最新一期
#macro($getLastest())
SELECT * FROM coins_lo_season where status!=2 ORDER BY id DESC limit 0,1
#end

##查询期别列表
#macro($seasonList())
SELECT * FROM coins_lo_season where STATUS != 2 and type = 1
union all
select * from(
		SELECT * FROM coins_lo_season WHERE id IN 
	(
		SELECT MAX(id) FROM coins_lo_season WHERE STATUS != 2 and type = 0 GROUP BY CODE
	) ORDER BY id DESC
) t

#end

##通过活动编号查询该活动所有的期别
#macro($querySeasonsByCode(code))
SELECT * FROM coins_lo_season where code=:code
#end