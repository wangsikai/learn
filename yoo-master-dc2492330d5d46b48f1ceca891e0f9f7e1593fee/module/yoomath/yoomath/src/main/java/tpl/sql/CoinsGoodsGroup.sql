## 查询全部的金币商品组
#macro($findAll())
SELECT t.* FROM coins_goods_group t WHERE t.status = 0 ORDER BY t.sequence ASC, id DESC
#end