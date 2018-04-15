## 获取指定年月的渠道相关已完成的统计
#macro($taskFindByChannel(channelCode, year, month))
select t.* from member_package_order_settlement t where t.user_channel_code=:channelCode
 and t.settlement_year=:year and t.settlement_month=:month
#end

## 获取某渠道返还利润的时间点及总数
#macro($findRefundDatas(channelCode))
select MAX(t.profits_gap_at) as pga,SUM(t.profits_gap) as pg from member_package_order_settlement t
 where t.user_channel_code=:channelCode
#end