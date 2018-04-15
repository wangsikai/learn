##通过抽奖商品id、期别id获取
#macro($getByKey(coinsLotteryGoodsId,seasonId))
SELECT * FROM  coins_lo_season_goods WHERE coins_lo_goods_id = :coinsLotteryGoodsId AND season_id =:seasonId
#end

##通过抽奖期别id获取商品列表
#macro($findBySeasonId(seasonId))
SELECT * FROM  coins_lo_season_goods WHERE season_id =:seasonId ORDER BY sequence  ASC
#end

