## 查找当前最新一期的数据
#macro($findEnable())
SELECT t.* FROM coins_lo_season t ORDER BY t.id DESC LIMIT 1
#end

## 更新一期净收入数据
#macro($updateEarnCoins(id,earnCoins))
UPDATE coins_lo_season SET earn_coins = :earnCoins WHERE id = :id
#end


## 查询活动里最大的code
#macro($getMaxActiveCode())
select MAX(code) from coins_lo_season
#end

## 根据活动code查询最新的一期
#macro($findNewestByCode(code))
SELECT t.* FROM coins_lo_season t WHERE t.code = :code ORDER BY t.id DESC LIMIT 1
#end
