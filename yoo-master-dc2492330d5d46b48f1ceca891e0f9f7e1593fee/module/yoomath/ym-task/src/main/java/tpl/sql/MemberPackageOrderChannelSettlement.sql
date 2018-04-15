#macro($getByDate(channelCode,year,month))
select * from member_package_order_channel_settlement
 where channel_code=:channelCode AND settlement_year=:year AND settlement_month=:month
#end

## 获得已返还利润的统计
#macro($getRefundSettle(channelCode))
select * from member_package_order_channel_settlement where channel_code=:channelCode AND profits_gap > 0
#end