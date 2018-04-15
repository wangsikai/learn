##游标查询未支付订单ID 集合
#macro($findMemberPackageOrderByNotPay(nowTime))
	SELECT * FROM member_package_order mpo WHERE mpo.type =0 and mpo.pay_mod=1 and  mpo.status =0 AND payment_platform_code is not null AND mpo.id > :next
	#if(nowTime)
		AND mpo.order_at >= :nowTime
	#end
	ORDER BY id ASC
#end

## 获取指定年月的渠道相关已完成订单
#macro($findByChannel(channelCode, bt, et))
select t.* from member_package_order t left join user u on u.id=t.user_id
 where t.source=1 and t.status=3 and t.order_at>=:bt and t.order_at<et 
 and (t.user_id=:channelCode or u.user_channel_code=:channelCode)
 order by t.id ASC
#end