## 查询最新的订单兑换信息
#macro($findLatestOrder(userType,userId,limitValue))
SELECT g.name, a.name AS user_name FROM coins_goods_order t INNER JOIN user u ON  u.id = t.user_id
 inner join account a on a.id = u.account_id
INNER JOIN goods g ON t.goods_id = g.id
WHERE t.status = 3 AND u.user_type = :userType AND t.source = 0
#if(userId)
AND u.id != :userId
#end
ORDER BY t.update_at DESC
LIMIT :limitValue
#end

## 查询订单
#macro($query(userId,orderSource,ignoreActivity))
SELECT t.* FROM
(
  SELECT a.* FROM coins_goods_order a
  INNER JOIN coins_lo_goods c ON a.coins_goods_id = c.id
  WHERE a.user_id = :userId AND a.del_status = 0 AND a.status IN (1, 2, 3, 4) AND c.level < 4 AND (c.coins_goods_type < 2 OR c.coins_goods_type > 3)
  #if(orderSource && orderSource != 2)
  	AND a.source = :orderSource
  #end
  #if(orderSource && orderSource == 2)
  	AND (a.source = 2 or a.source = 3)
  #end
  UNION ALL
  SELECT b.* FROM coins_goods_order b
  INNER JOIN coins_goods m ON b.coins_goods_id = m.id
  WHERE b.user_id = :userId AND b.del_status = 0 AND b.status IN (1, 2, 3, 4)
  #if(orderSource && orderSource != 2)
  	AND b.source = :orderSource
  #end
  #if(orderSource && orderSource == 2)
  	AND (b.source = 2 or b.source = 3)
  #end
  #if(ignoreActivity==false)
  UNION ALL
  SELECT d.* FROM coins_goods_order d
  INNER JOIN lottery_activity_goods n ON d.coins_goods_id = n.id
  WHERE d.user_id = :userId AND d.del_status = 0 AND d.status IN (1, 2, 3, 4) AND (n.activity_goods_type < 2 OR n.activity_goods_type > 3)
  #if(orderSource && orderSource != 2)
  	AND d.source = :orderSource
  #end
   #if(orderSource && orderSource == 2)
  	AND (d.source = 2 or d.source = 3)
  #end
  #end
) t
#if(next)
	WHERE t.id < :next
#end
ORDER BY t.id DESC
#end

## 查询用户抽奖未完成信息的数据
#macro($queryLotteryUnFinish(userId, seasonId))
SELECT t.* FROM coins_goods_order t
WHERE t.user_id = :userId AND t.del_status = 0 AND t.p0 IS NULL AND t.status = 0
AND t.source = 1 AND t.p1 = :seasonId
#end

## 查询用户当天购买某商品的数量
#macro($countTodayCoinsGoodsBuyCount(coinsGoodsId,userId,nowDate))
SELECT COUNT(id) FROM coins_goods_order 
WHERE coins_goods_id = :coinsGoodsId AND user_id = :userId AND status != 4 
AND date_format(order_at,'%Y-%m-%d') = date_format(:nowDate,'%Y-%m-%d') 
#end

## 查询用户一期内是否已经中了大奖
#macro($queryUserSeasonLottery(seasonId, level, userId))
SELECT t.* FROM coins_goods_order t
INNER JOIN coins_lo_goods c ON c.id = t.coins_goods_id
WHERE t.p1 = :seasonId AND c.level = :level AND t.user_id = :userId
#end

##查询抽奖活动订单数据
#macro($queryLotteryActivityOrderPage(activityCode, userId, orderSource))
SELECT t.* FROM coins_goods_order t
inner join lottery_activity_goods lg on lg.id=t.goods_id
where t.source=:orderSource AND lg.activity_goods_type!=3
#if(userId)
 AND t.user_id=:userId
#end
#if(activityCode)
 AND lg.activity_code=:activityCode
#end
 order by t.order_at DESC
#end

##查询假期抽奖活动订单数据
#macro($queryLotteryActivityOrder(userId, orderSource, seasonId))
SELECT t.id, 
gl.level,
t.user_id,
g.name,
u.name userName
FROM coins_goods_order t,
coins_lo_goods gl,
goods g,
user u
WHERE t.source=:orderSource
AND t.goods_id = g.id
AND t.goods_id = gl.id
AND t.user_id= u.id
#if(userId)
 AND t.user_id=:userId
#end
#if(seasonId)
 AND t.p1=:seasonId
#end
order by t.order_at DESC;
#end

## 查询用户抽奖未完成信息的数据
#macro($queryLotteryUnFinishBySource(userId, seasonId, orderSource))
SELECT t.* FROM coins_goods_order t
WHERE t.user_id = :userId AND t.del_status = 0 AND t.p0 IS NULL AND t.status = 0
AND t.source = :orderSource AND t.p1 = :seasonId
#end