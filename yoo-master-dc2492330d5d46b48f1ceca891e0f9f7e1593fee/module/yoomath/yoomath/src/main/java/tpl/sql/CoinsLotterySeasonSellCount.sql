## 查询一期内的商品销售数量
#macro($findLotterySellCount(seasonId))
SELECT t.* FROM coins_lo_sean_sell_count t WHERE t.season_id = :seasonId
#end

## 根据商品及期别查询数据
#macro(findByGoodsAndSeason(seasonId,goodsId))
SELECT t.* FROM coins_lo_sean_sell_count t WHERE t.season_id = :seasonId AND t.coins_lo_goods_id = :goodsId
#end