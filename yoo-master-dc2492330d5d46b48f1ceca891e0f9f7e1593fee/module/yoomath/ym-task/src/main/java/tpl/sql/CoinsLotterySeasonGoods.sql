## 查找一期内对应的抽奖商品数量
#macro($findBySeason(seasonId))
SELECT t.* FROM coins_lo_season_goods t WHERE t.season_id = :seasonId
#end