## 根据条件查询订单
#macro($queryOrderList(accountName,status,startTime,endTime,orderType,userType,source))
	select t.* from
	(
	select a.* from coins_goods_order a 
	inner join coins_goods d on a.goods_id =d.id
	inner join user b on a.user_id = b.id
	#if(accountName)
		inner join account c on b.account_id = c.id
	#end
	where 1=1
	#if(status)
		and a.status = :status
	#end
	#if(accountName)
		and c.name like :accountName
	#end
	#if(startTime)
		and a.order_at > :startTime
	#end
	#if(endTime)
		and a.order_at < :endTime
	#end
	#if(orderType)
		#if(orderType=='-1')
			and d.coins_goods_type is null
		#else
			and d.coins_goods_type =:orderType
		#end
	#end
	#if(userType)
		and b.user_type = :userType
	#end
	#if(source)
		and a.source = :source
	#end
	union all
	select a.* from coins_goods_order a 
	inner join coins_lo_goods_snapshot ff on a.conis_goods_snapshot_id = ff.id
	inner join user b on a.user_id = b.id
	#if(accountName)
		inner join account c on b.account_id = c.id
	#end
	where 1=1
	#if(status)
		and a.status = :status
	#end
	and a.status not in (0,5)
	#if(accountName)
		and c.name like :accountName
	#end
	#if(startTime)
		and a.order_at > :startTime
	#end
	#if(endTime)
		and a.order_at < :endTime
	#end
	#if(orderType)
		and ff.coins_goods_type in (:orderType)
	#end
	and ff.coins_goods_type in (0,1,6,7)
	#if(userType)
		and b.user_type = :userType
	#end
	#if(source)
		and a.source = :source
	#end
	union all
	select a.* from coins_goods_order a 
    INNER JOIN lottery_activity_goods_snapshot lags on a.conis_goods_snapshot_id =lags.id
    and lags.activity_goods_type not in  (2,3)
    INNER JOIN user b on a.user_id =b.id
    #if(accountName)
		inner join account c on b.account_id = c.id
	#end
	where 1=1
	#if(status)
		and a.status = :status
	#end
	and a.status not in (0,5)
	#if(accountName)
		and c.name like :accountName
	#end
	#if(startTime)
		and a.order_at > :startTime
	#end
	#if(endTime)
		and a.order_at < :endTime
	#end
	#if(orderType)
		and lags.activity_goods_type in (:orderType)
	#end
	#if(orderType==0)
	    or lags.activity_goods_type = 5
	#end
	#if(userType)
		and b.user_type = :userType
	#end
	#if(source)
		and a.source = :source
	#end
	) t
	order by t.order_at desc
#end

## 查询待兑换的商品个数
#macro($tobeExchageCount())
	select count(id) from coins_goods_order where status = 1 and del_status = 0
#end

## 根据类型查询统计各类型在不同的时间段的兑换情况
#macro($zycStatisticByType(beginDate,endDate,virtualType,notVirtualType))
SELECT sum(t.amount) AS amount, sum(t.total_price) AS total_price, m.coins_goods_type as coins_goods_type
FROM coins_goods_order t INNER JOIN coins_goods m ON m.id = t.coins_goods_id
WHERE t.status = 3
#if(beginDate)
AND t.update_at >= :beginDate
#end
#if(endDate)
AND t.update_at <= :endDate
#end
#if(virtualType)
AND m.coins_goods_type =:virtualType
#end
#if(notVirtualType)
AND m.coins_goods_type is null
#end
GROUP BY m.coins_goods_type
#end

## 根据商品名称进行统计
#macro($zycStatisticByName(beginDate,endDate,virtualType,notVirtualType))
SELECT sum(t.amount) AS amount, sum(t.total_price) AS total_price, g.name AS good_name
FROM coins_goods_order t INNER JOIN coins_goods m ON m.id = t.coins_goods_id
INNER JOIN goods g ON g.id = t.goods_id
WHERE t.status = 3
#if(beginDate)
AND t.update_at >= :beginDate
#end
#if(endDate)
AND t.update_at <= :endDate
#end
#if(virtualType)
AND m.coins_goods_type =:virtualType
#end
#if(notVirtualType)
AND m.coins_goods_type is null
#end
GROUP BY g.id
#end

##抽奖商品统计(商品名称、中奖数量、奖品价值金币)
#macro($queryLotteryOrderStatis(seasonId,startAt,endAt,code,type))
SELECT c.name,SUM(a.amount) amount,SUM(c.price) total_price FROM coins_goods_order a 
INNER JOIN coins_lo_goods_snapshot b ON  a.conis_goods_snapshot_id = b.id
INNER JOIN goods_snapshot c ON a.goods_snapshot_id = c.id
#if(code)
	INNER JOIN coins_lo_season d ON a.p1 = d.id
	AND d.code = :code 
#end
#if(type == 0)
	AND a.source = 1
#end
#if(type == 1)
	AND a.source = 3
#end
#if(seasonId)
	AND a.p1 = :seasonId
#end
#if(startAt)
	AND a.order_at >= :startAt
#end
#if(endAt)
	AND a.order_at < :endAt
#end
GROUP BY c.name
#end

##抽奖商品总体统计(参与用户、总抽奖次数、总收入金币、总奖品价值金币、净收入)
#macro($lotteryTotalStatis(seasonId,startAt,endAt,code,type))
SELECT SUM(c.price) goods_total_price,SUM(a.amount) amount,
	   SUM(a.total_price) total_income,COUNT(DISTINCT a.user_id) usernum FROM coins_goods_order a 
INNER JOIN coins_lo_goods_snapshot b ON  a.conis_goods_snapshot_id = b.id
INNER JOIN goods_snapshot c ON a.goods_snapshot_id = c.id
#if(code)
	INNER JOIN coins_lo_season d ON a.p1 = d.id
	AND d.code = :code 
#end
#if(type == 0)
	AND a.source = 1
#end
#if(type == 1)
	AND a.source = 3
#end
#if(seasonId)
	AND a.p1 = :seasonId
#end
#if(startAt)
	AND a.order_at >= :startAt
#end
#if(endAt)
	AND a.order_at < :endAt
#end
#end

##用户抽奖记录(抽奖时间、账户名、身份、是否中奖、中奖奖品)
#macro($lotteryUserRecordList(seasonId,startAt,endAt,userType,accountName,justLookWin,code,type))
SELECT a.order_at ,a.user_id,c.name goodsname,b.level,d.user_type,e.name accountname FROM coins_goods_order a 
INNER JOIN coins_lo_goods_snapshot b ON  a.conis_goods_snapshot_id = b.id
INNER JOIN goods_snapshot c ON a.goods_snapshot_id = c.id
INNER JOIN USER d ON a.user_id = d.id
INNER JOIN account e ON d.account_id = e.id
#if(code)
	INNER JOIN coins_lo_season f ON a.p1 = f.id
	AND f.code = :code 
#end
#if(type == 0)
	AND a.source = 1
#end
#if(type == 1)
	AND a.source = 3
#end
	#if(seasonId)
		AND a.p1 = :seasonId
	#end
	#if(startAt)
		AND a.order_at >= :startAt
	#end
	#if(endAt)
		AND a.order_at < :endAt
	#end
	#if(userType)
		AND d.user_type =:userType
	#end
	#if(justLookWin)
		AND b.level !=4
	#end
	#if(accountName)
		AND e.name like :accountName
	#end
ORDER BY a.order_at DESC
#end

