## 取消订单，修改相应的值
#macro($updateDaySellCount(goodsId,date0,count1))
	update coins_goods_day_sell_count set count0 = count0 +:count1
	where coins_goods_id =:goodsId and date0 =:date0
#end