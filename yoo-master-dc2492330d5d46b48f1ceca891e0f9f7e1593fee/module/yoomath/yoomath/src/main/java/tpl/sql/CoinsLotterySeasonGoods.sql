## 根据季度及商品取数据
#macro($findByGoodsAndSeasonId(seasonId, goodsId))
SELECT t.* FROM coins_lo_season_goods t WHERE t.season_id = :seasonId AND t.coins_lo_goods_id = :goodsId
#end