## 查询渠道商结算列表
#macro($csSettlementList(channelCode,year))
	SELECT * FROM member_package_order_channel_settlement
	WHERE settlement_year =:year AND channel_code = :channelCode order by settlement_month ASC 
#end

## 查询渠道商结算排名
#macro($csQueryChannelSalesRank(year,month))
	SELECT t.* FROM(
		SELECT SUM(transaction_amount) amount1,
		SUM(channel_profits) amount2,
		SUM(profits) amount3,a.channel_code,b.name 
		FROM member_package_order_channel_settlement a
		INNER JOIN user_channel b ON a.channel_code = b.code
		#if(year)
		   AND settlement_year = :year
		#end
		#if(month)
		   AND settlement_month = :month
		#end
		GROUP BY a.channel_code
	)t ORDER BY t.amount1 DESC
#end

## 查询渠道商结算排名(全年的要算上渠道分成差额)
#macro($csQueryChannelSalesAllYearRank(year))
	SELECT t.* FROM(
		SELECT SUM(transaction_amount) amount1,
		SUM(channel_profits)+SUM(profits_gap) amount2,
		SUM(profits)-SUM(profits_gap) amount3,a.channel_code,b.name 
		FROM member_package_order_channel_settlement a
		INNER JOIN user_channel b ON a.channel_code = b.code
		#if(year)
		   AND settlement_year = :year
		#end
		#if(month)
		   AND settlement_month = :month
		#end
		GROUP BY a.channel_code
	)t ORDER BY t.amount1 DESC
#end

## 查询全年统计数据
#macro($csAllYearStat(channelCode,year,month))
	SELECT 	SUM(transaction_amount) all_amount,
			SUM(channel_profits)+SUM(profits_gap) channel_amount,
			SUM(profits)-SUM(profits_gap) my_amount 
	FROM member_package_order_channel_settlement
	WHERE 1=1
	#if(channelCode)
		AND channel_code = :channelCode
	#end
	#if(year)
		AND settlement_year =:year
	#end
	#if(month)
		AND settlement_month =:month
	#end
#end