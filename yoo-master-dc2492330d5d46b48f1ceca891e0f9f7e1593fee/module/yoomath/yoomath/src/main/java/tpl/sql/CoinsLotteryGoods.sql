## 按照顺序查找所有的抽奖奖品
#macro($findAll(seasonId))
SELECT t.* FROM coins_lo_goods t
INNER JOIN coins_lo_season_goods c ON t.id = c.coins_lo_goods_id
WHERE c.season_id = :seasonId
ORDER BY c.sequence ASC
#end

## 根据奖品等级查找数据
#macro($findByLevel(level,seasonId))
SELECT t.* FROM coins_lo_goods t
INNER JOIN coins_lo_season_goods c ON c.coins_lo_goods_id = t.id
WHERE t.level = :level
AND c.season_id = :seasonId
#end