#macro($getResourcesGoodsOrders(userId,type,status,delStatus))
SELECT * FROM resources_goods_order WHERE user_id=:userId and type=:type and status=:status and del_status=:delStatus
 ORDER BY order_at DESC
#end

##通过用户及商品查询已完成的未删除订单.
#macro($findCompleteOrderByUserAndGoods(userID, goodsID))
SELECT * FROM resources_goods_order WHERE status=3 AND user_id=:userID AND resources_goods_id=:goodsID
#end