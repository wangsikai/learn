## 金币商品兑换数量更新
#macro($incrCount(coinsGoodsId,date0,delta,maxCount0))
UPDATE coins_goods_day_sell_count 
SET count0 = count0 + :delta
WHERE coins_goods_id = :coinsGoodsId AND date0 = :date0 AND count0 <= :maxCount0
#end

## 查找
#macro($get(coinsGoodsId,date0))
SELECT * FROM coins_goods_day_sell_count
WHERE coins_goods_id = :coinsGoodsId AND date0 = :date0
#end

## 查找
#macro($mget(coinsGoodsIds,date0))
SELECT * FROM coins_goods_day_sell_count
WHERE coins_goods_id IN (:coinsGoodsIds) AND date0 = :date0
#end