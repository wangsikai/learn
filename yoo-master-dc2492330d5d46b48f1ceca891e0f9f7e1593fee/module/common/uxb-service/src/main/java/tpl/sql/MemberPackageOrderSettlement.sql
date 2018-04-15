#macro($findByOrder(orderId))
select * from member_package_order_settlement where order_id=:orderId
#end

## 获取渠道订单统计个数，已返还差额
#macro($countMember(userChannelCode))
select sum(member_count*year_count) as mc,sum(profits_gap) as pg from member_package_order_settlement
 where user_channel_code=:userChannelCode
#end

## 利润返还更新
#macro($refund(userChannelCode, profitsGap, profitsGapAt))
update member_package_order_settlement set profits_gap=:profitsGap*member_count*year_count, profits_gap_at=:profitsGapAt
 where user_channel_code=:userChannelCode and create_at<:profitsGapAt
#end

## 返润结算使用-查询渠道所有统计
#macro($queryRefunds(userChannelCode))
select * from member_package_order_settlement where user_channel_code=:userChannelCode order by create_at ASC
#end

## 查询渠道所有统计对应的套餐组
#macro($queryGroupsBySettles(userChannelCode))
SELECT g.* FROM member_package_group g
 WHERE EXISTS (
  SELECT 1 FROM member_package_order_settlement s
  WHERE s.user_channel_code=:userChannelCode AND s.`member_package_group_id`=g.id
)
#end

##获取所有未做返还的，且总阈值超过或等于指定阈值的渠道
#macro($findAllNotRefundChannel(divideNum))
SELECT t.user_channel_code AS code, SUM(t.member_count * t.year_count) AS dcount FROM member_package_order_settlement t
 WHERE NOT EXISTS
 (
 SELECT 1 FROM member_package_order_settlement m WHERE m.profits_gap_at IS NOT NULL AND m.user_channel_code=t.user_channel_code
 )
 GROUP BY t.user_channel_code HAVING SUM(t.member_count * t.year_count)>=:divideNum
#end