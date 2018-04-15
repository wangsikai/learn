##查询套餐订单
#macro($csSearchUserMemberOrders(orderCode,payCode,channelName,accountName,startAt,endAt,userType,memberType,type,status,queryNameType,openType,payMod,paymentPlatformCode,thirdPaymentMethod,qualifier,allStatus))
select mpo.*,mp.month0,mpc.month0 as month1,mp.present_rice,u.user_type,a.name as accountname,uc.name as channelname,
IFNULL(mpo.payment_code, mpo.payment_platform_order_code) as `payCode`,mpos.channel_profits as channelProfits,mpos.profits as profits
from member_package_order mpo 
LEFT  JOIN member_package_card mpc on mpc.order_id=mpo.id
LEFT  JOIN member_package mp on mp.id=mpo.member_package_id
LEFT  JOIN user u on mpo.user_id =u.id
LEFT  JOIN account a on a.id=u.account_id 
LEFT  JOIN user_channel uc on uc.code=mpo.user_channel_code 
LEFT  JOIN member_package_order_settlement mpos on mpos.order_id=mpo.id
WHERE  mpo.type =:type and mpo.status in (0,1,2,3,6)
#if(status==0)
and mpo.status=0 and mpo.order_at>:qualifier
#elseif(status==6)
and mpo.status=0 and mpo.order_at<=:qualifier
#elseif(allStatus==false)
and mpo.status=:status
#end
#if(payMod==3)
and mpo.pay_mod=3
#elseif(paymentPlatformCode==2)
and mpo.payment_platform_code=:paymentPlatformCode
#elseif(paymentPlatformCode==1)
and mpo.third_payment_method=:thirdPaymentMethod
#end
#if(memberType)
and  mpo.member_type=:memberType
#end
#if(orderCode)
and mpo.code = :orderCode
#end
#if(payCode)
and (mpo.payment_platform_order_code = :payCode or payment_code =:payCode)
#end
#if(channelCode)
and uc.code = :channelCode
#end
#if(channelName)
and uc.name like :channelName
#end
#if(queryNameType==1)
and a.name like :accountName
#elseif(queryNameType==2)
and EXISTS (
select 1 from member_package_order_user mpou 
INNER JOIN user u1 on mpou.user_id =u1.id  
INNER JOIN account a1 on u1.account_id=a1.id 
where a1.name like :accountName
and mpou.member_package_order_id=mpo.id
)
#end
#if(startAt)
 and mpo.order_at>:startAt
#end
#if(endAt)
 and mpo.order_at<=:endAt
#end
#if(userType)
 and ( mp.user_type =:userType or exists(
select 1 from member_package_order_user mpou INNER JOIN user u2 ON 
u2.id=mpou.user_id AND u2.user_type=:userType  where mpo.id=mpou.member_package_order_id 
) )
#end
ORDER BY mpo.order_at DESC
#end

##查询错题代印订单
#macro($csSearchToOneOrderUser(orderId))
select count(1) as countpaper,mpou.member_package_order_id as orderid,a.name as accountname,u.user_type
,mpou.deadline as enddate
from member_package_order_user mpou 
INNER JOIN user u on mpou.user_id =u.id 
INNER JOIN account a on a.id=u.account_id 
where member_package_order_id 
in :orderId GROUP BY member_package_order_id
#end



##查询错题代印订单
#macro($csSearchFallibleQuestionPrintOrders(key,startAt,endAt,paymod))
select mpo.*,u.user_type,a.name as accountname,uc.name as channelname from
fallible_question_print_order mpo 
INNER JOIN user u on mpo.user_id =u.id 
INNER JOIN account a on a.id=u.account_id 
INNER JOIN user_channel uc on u.user_channel_code =uc.code 
WHERE mpo.status>=1 and mpo.status<6
#if(key)
 and ((a.name like :key) or (mpo.code like :key) or (uc.name like :key))
#end
#if(startAt)
 and mpo.order_at>:startAt
#end
#if(endAt)
 and mpo.order_at<=:endAt
#end
ORDER BY mpo.order_at DESC
#end

##查询试卷商品订单
#macro($csSearchResourcesGoodsOrders(key,startAt,endAt,status,paymod))
select mpo.*,u.user_type,a.name as accountname,uc.name as channelname from
resources_goods_order mpo 
INNER JOIN user u on mpo.user_id =u.id 
INNER JOIN account a on a.id=u.account_id 
INNER JOIN user_channel uc on u.user_channel_code =uc.code 
WHERE mpo.status=:status
#if(key)
 and ((a.name like :key) or (mpo.code like :key) or (uc.name like :key))
#end
#if(startAt)
 and mpo.order_at>:startAt
#end
#if(endAt)
 and mpo.order_at<=:endAt
#end
#if(paymod)
 and mpo.pay_mod=:paymod
#end
ORDER BY mpo.order_at DESC
#end

##通过订单查询帐号
#macro($csSearchUserByOrder(orderId))
select a.name from member_package_order_user mpu 
INNER JOIN user u on mpu.user_id=u.id 
INNER JOIN account a on u.account_id=a.id 
where mpu.member_package_order_id = :orderId
#end

##查询卡片激活记录
#macro($queryMemberCardList(startAt,endAt,keystr))
	SELECT a.order_at,a.virtual_card_code,d.name,b.memo,b.user_type,b.member_type,b.price FROM member_package_order a 
	INNER JOIN member_package_card b ON a.virtual_card_code =  b.code
	INNER JOIN USER c ON a.user_id = c.id 
	INNER JOIN account d ON c.account_id = d.id
	WHERE a.virtual_card_type = 1
	#if(keystr)
		AND (d.name like :keystr or b.memo like :keystr)
	#end
	#if(startAt)
		AND a.order_at >= :startAt
	#end
	#if(endAt)
		AND a.order_at <= :endAt
	#end
	ORDER BY a.order_at DESC
#end

##查询会员订单相关统计
#macro($queryBaseStatByCondition(cardType,source,orderType,userType,memberType,timeStr,startTime,endTime))
	SELECT SUM(total_price) price FROM member_package_order a 
	LEFT JOIN member_package mp on mp.id=a.member_package_id
	WHERE virtual_card_type = :cardType and a.status in (1,2,3)
	#if(source)
		AND source = :source 
	#end
	#if(orderType)
		AND TYPE = :orderType
	#end
	#if(userType)
		and  (mp.user_type =:userType or EXISTS (
	  select * from member_package_order_user mpou 
	  INNER JOIN user u1 on mpou.user_id =u1.id  
	  where u1.user_type =:userType
	  and mpou.member_package_order_id=a.id))
	#end
	#if(memberType)
		AND a.member_type = :memberType
	#end
	#if(timeStr)
		AND a.order_at like :timeStr
	#end
	#if(startTime)
		AND a.order_at >= :startTime
	#end
	#if(endTime)
		AND a.order_at <= :endTime
	#end
#end


##查询错题打印相关统计
#macro($queryFallPrintStat(isChannel,timeStr,startTime,endTime))
	SELECT SUM(total_price) FROM fallible_question_print_order a 
	INNER JOIN USER b ON a.user_id = b.id 
	where a.status NOT IN (0,6,7)
	#if(isChannel == true)
		AND b.user_channel_code != 10000
	#elseif(isChannel == false)
		AND b.user_channel_code = 10000
	#end
	#if(timeStr)
		AND a.order_at like :timeStr
	#end
	#if(startTime)
		AND a.order_at >= :startTime
	#end
	#if(endTime)
		AND a.order_at <= :endTime
	#end
#end

##查询购买试卷相关统计
#macro($queryBuyPaperStat(isChannel,timeStr,startTime,endTime))
	SELECT SUM(total_price) FROM resources_goods_order a 
	INNER JOIN USER b ON a.user_id = b.id 
	AND a.status IN (1,2,3)
	#if(isChannel == true)
		AND b.user_channel_code != 10000
	#elseif(isChannel == false)
		AND b.user_channel_code = 10000
	#end
	#if(timeStr)
		AND a.order_at like :timeStr
	#end
	#if(startTime)
		AND a.order_at >= :startTime
	#end
	#if(endTime)
		AND a.order_at <= :endTime
	#end
#end

##会员套餐统计
#macro($csQueryMemberPackageOrderStat(userType,memberType,startAt,endAt))
select  mpo.member_package_id, mp.month0,mp.original_price,mp.present_rice,sum(mpo.amount) as count,
sum(mpo.total_price) as total_price  from member_package_order mpo INNER JOIN member_package mp 
on mp.id=mpo.member_package_id where  mp.user_type =:userType 
and mpo.status=3
and mpo.virtual_card_type=0
and mpo.member_type=:memberType
#if(startAt)
 and mpo.order_at>:startAt
#end
#if(endAt)
 and mpo.order_at<=:endAt
#end
GROUP BY  mpo.member_package_id
ORDER BY total_price DESC
#end


##会员套餐统计总金额
#macro($csQueryMemberPackageOrderSumStat(userType,memberType,startAt,endAt))
select sum(t.total_price) as total_price,t.member_package_id from 
(select sum(mpo.total_price) as total_price,CASE WHEN  mpo.member_package_id = 0  
THEN 0 ELSE 1 end as 'member_package_id'  
from member_package_order mpo LEFT  JOIN member_package mp 
on mp.id=mpo.member_package_id where  (mp.user_type =:userType or EXISTS (
  select * from member_package_order_user mpou 
  INNER JOIN user u1 on mpou.user_id =u1.id  
  where u1.user_type =:userType
  and mpou.member_package_order_id=mpo.id
))
and mpo.status=3
and mpo.virtual_card_type=0
and mpo.member_type=:memberType
#if(startAt)
 and mpo.order_at>:startAt
#end
#if(endAt)
 and mpo.order_at<=:endAt
#end
GROUP BY member_package_id) as t GROUP BY t.member_package_id
#end

##查询会员订单有数据的年份列表
#macro($cardYearList())
	SELECT DISTINCT LEFT(order_at,4) FROM member_package_order WHERE status IN (1,2,3) order by order_at desc
#end

##查询试卷购买有数据的年份列表
#macro($resourceGoodsYearList())
	SELECT DISTINCT LEFT(order_at,4) FROM resources_goods_order WHERE status IN (1,2,3) order by order_at desc
#end

##查询错题打印有数据的年份列表
#macro($fallPrintYearList())
	SELECT DISTINCT LEFT(order_at,4) FROM fallible_question_print_order WHERE status IN (1,2,3,4,5) order by order_at desc
#end

## 对交易进行查询(渠道商统计) 说明，会返回code,name
## price1 -> price7 分别代表 教师自主开通，学生自主开通，后台为教师开通，后台为学生开通，代打印服务，购买试卷，开通校级vip。
## 注意一下，兑换会员的方式有通过会员卡进行兑换的，这部分统计不计入渠道商统计
#macro($queryByChannel(name,startTime,endTime,year,month))
SELECT
  n.*,
  (n.price1 + n.price3)                       teaPrice,
  (n.price2 + n.price4)                       stuPrice,
  (n.price1 + n.price2 + n.price3 + n.price4 + n.price5 + n.price6 + price7) totalPrice
FROM
  (
    SELECT
      z.name,
      z.code,
      sum(ifnull(z.price1, 0)) price1,
      sum(ifnull(z.price2, 0)) price2,
      sum(ifnull(z.price3, 0)) price3,
      sum(ifnull(z.price4, 0)) price4,
      sum(ifnull(z.price5, 0)) price5,
      sum(ifnull(z.price6, 0)) price6,
      sum(ifnull(z.price7, 0)) price7
    FROM (
           SELECT
             m.name,
             m.code,
             CASE m.vn
             WHEN 1
               THEN m.price
             END
               AS price1,
             CASE m.vn
             WHEN 2
               THEN m.price
             END
               AS price2,
             CASE m.vn
             WHEN 3
               THEN m.price
             END
               AS price3,
             CASE m.vn
             WHEN 4
               THEN m.price
             END
               AS price4,
             CASE m.vn
             WHEN 5
               THEN m.price
             END
               AS price5,
             CASE m.vn
             WHEN 6
               THEN m.price
             END
               AS price6,
             CASE m.vn
             WHEN 7
               THEN m.price
             END
               AS price7
           FROM (
                  SELECT
                    uc.name,
                    IFNULL(sum(t.total_price), 0) price,
                    uc.code,
                    1 AS                          vn
                  FROM member_package_order t
                    INNER JOIN user u ON u.id = t.user_id
                    INNER JOIN teacher te ON te.id = u.id
                    INNER JOIN user_channel uc ON uc.code = u.user_channel_code
                    INNER JOIN member_package_order_user mo ON mo.user_id = u.id
                  WHERE uc.code > 10000 AND t.source = 0 AND t.status = 3 AND mo.member_type = 1 AND (t.pay_mod IS NULL OR t.pay_mod < 3)
                  #if(name)
                  AND uc.name LIKE :name
                  #end
                  #if(startTime)
                  AND t.order_at >= :startTime
                  #end
                  #if(endTime)
                  AND t.order_at <= :endTime
                  #end
                  #if(year)
                  AND date_format(t.order_at, '%Y') = :year
                  #end
                  #if(month)
                  AND date_format(t.order_at, '%m') = :month
                  #end
                  GROUP BY uc.code, t.type

                  UNION

                  SELECT
                    uc.name,
                    sum(t.total_price) price,
                    uc.code,
                    2 AS               vn
                  FROM member_package_order t
                    INNER JOIN user u ON u.id = t.user_id
                    INNER JOIN student te ON te.id = u.id
                    INNER JOIN user_channel uc ON uc.code = u.user_channel_code
                    INNER JOIN member_package_order_user mo ON mo.user_id = u.id
                  WHERE uc.code > 10000 AND t.source = 0 AND t.status = 3 AND mo.member_type = 1 AND (t.pay_mod IS NULL OR t.pay_mod < 3)
                  #if(name)
                  AND uc.name LIKE :name
                  #end
                  #if(startTime)
                  AND t.order_at >= :startTime
                  #end
                  #if(endTime)
                  AND t.order_at <= :endTime
                  #end
                  #if(year)
                  AND date_format(t.order_at, '%Y') = :year
                  #end
                  #if(month)
                  AND date_format(t.order_at, '%m') = :month
                  #end
                  GROUP BY uc.code, t.type

                  UNION

                  SELECT
                    uc.name,
                    sum(t.total_price) price,
                    uc.code,
                    3 AS               vn
                  FROM member_package_order t
                    INNER JOIN member_package_order_user mpo ON mpo.member_package_order_id = t.id
                    INNER JOIN user_channel uc ON uc.code = t.user_id
                    INNER JOIN teacher te ON te.id = mpo.user_id
                  WHERE uc.code > 10000 AND t.source = 1 AND t.status = 3 AND mpo.member_type = 1 AND (t.pay_mod IS NULL OR t.pay_mod < 3)
                  #if(name)
                  AND uc.name LIKE :name
                  #end
                  #if(startTime)
                  AND t.order_at >= :startTime
                  #end
                  #if(endTime)
                  AND t.order_at <= :endTime
                  #end
                  #if(year)
                  AND date_format(t.order_at, '%Y') = :year
                  #end
                  #if(month)
                  AND date_format(t.order_at, '%m') = :month
                  #end
                  GROUP BY uc.code, t.type

                  UNION

                  SELECT
                    uc.name,
                    sum(t.total_price) price,
                    uc.code,
                    4 AS               vn
                  FROM member_package_order t
                    INNER JOIN member_package_order_user mpo ON mpo.member_package_order_id = t.id
                    INNER JOIN user_channel uc ON uc.code = t.user_id
                    INNER JOIN student te ON te.id = mpo.user_id
                  WHERE uc.code > 10000 AND t.source = 1 AND t.status = 3 AND mpo.member_type = 1 AND (t.pay_mod IS NULL OR t.pay_mod < 3)
                  #if(name)
                  AND uc.name LIKE :name
                  #end
                  #if(startTime)
                  AND t.order_at >= :startTime
                  #end
                  #if(endTime)
                  AND t.order_at <= :endTime
                  #end
                  #if(year)
                  AND date_format(t.order_at, '%Y') = :year
                  #end
                  #if(month)
                  AND date_format(t.order_at, '%m') = :month
                  #end
                  GROUP BY uc.code, t.type

                  UNION

                 SELECT
                    uc.name,
                    sum(t.total_price) price,
                    uc.code,
                    5 AS               vn
                  FROM resources_goods_order t INNER JOIN user u ON t.user_id = u.id
                    INNER JOIN user_channel uc ON u.user_channel_code = uc.code
                  WHERE t.status = 3 AND uc.code > 10000 AND t.pay_mod = 1
                  #if(name)
                  AND uc.name LIKE :name
                  #end
                  #if(startTime)
                  AND t.order_at >= :startTime
                  #end
                  #if(endTime)
                  AND t.order_at <= :endTime
                  #end
                  #if(year)
                  AND date_format(t.order_at, '%Y') = :year
                  #end
                  #if(month)
                  AND date_format(t.order_at, '%m') = :month
                  #end

                  UNION
                  SELECT
                    uc.name,
                    sum(t.total_price) price,
                    uc.code,
                    6 AS               vn
                  FROM fallible_question_print_order t INNER JOIN user u ON u.id = t.user_id
                    INNER JOIN user_channel uc ON uc.code = u.user_channel_code
                  WHERE t.status IN (1, 2, 3, 4, 5) AND uc.code > 10000
                  #if(name)
                  AND uc.name LIKE :name
                  #end
                  #if(startTime)
                  AND t.order_at >= :startTime
                  #end
                  #if(endTime)
                  AND t.order_at <= :endTime
                  #end
                  #if(year)
                  AND date_format(t.order_at, '%Y') = :year
                  #end
                  #if(month)
                  AND date_format(t.order_at, '%m') = :month
                  #end

                  UNION
                  SELECT
                    uc.name,
                    sum(t.total_price) price,
                    uc.code,
                    7 AS               vn
                  FROM member_package_order t
                    INNER JOIN member_package_order_user mpo ON mpo.member_package_order_id = t.id
                    INNER JOIN user_channel uc ON uc.code = t.user_id
                    INNER JOIN teacher te ON te.id = mpo.user_id
                  WHERE uc.code > 10000 AND t.source = 1 AND t.status = 3 AND mpo.member_type = 2 AND (t.pay_mod IS NULL OR t.pay_mod < 3)
                  #if(name)
                  AND uc.name LIKE :name
                  #end
                  #if(startTime)
                  AND t.order_at >= :startTime
                  #end
                  #if(endTime)
                  AND t.order_at <= :endTime
                  #end
                  #if(year)
                  AND date_format(t.order_at, '%Y') = :year
                  #end
                  #if(month)
                  AND date_format(t.order_at, '%m') = :month
                  #end
                  GROUP BY uc.code, t.type
                ) m
           ) z WHERE z.code > 0
    GROUP BY z.code) n ORDER BY totalPrice DESC
#end

##查询会员卡金额，一旦创建认为已经交易(UE定)
#macro($queryCardTotal(userType,memberType,startTime,endTime,timeStr))
	select sum(price) total_price from member_package_card
	where price > 0
	#if(userType)
		and user_type = :userType 
	#end
	#if(memberType)
		and member_type = :memberType 
	#end
	#if(timeStr)
		AND create_time like :timeStr
	#end
	#if(startTime)
	 and create_time>:startTime
	#end
	#if(endTime)
	 and create_time<=:endTime
	#end
#end

