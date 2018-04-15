package com.lanking.uxb.channelSales.finance.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlement;

/**
 * 会员套餐订单渠道结算接口
 * 
 * @author wangsenhao
 *
 */
public interface CsMemberPackageOrderChannelSettlementService {

	/**
	 * 查询结算列表
	 * 
	 * @param year
	 * @param channelCode
	 * @return
	 */
	List<MemberPackageOrderChannelSettlement> list(Integer year, Integer channelCode);

	/**
	 * 渠道商销售排名
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> queryChannelSalesRank(Integer year, Integer month);

	/**
	 * 结算
	 * 
	 * @param id
	 */
	void settlement(long id);

	/**
	 * 全年统计数据
	 * 
	 * @param year
	 * @param channelCode
	 * @param month
	 *            只有整体统计里需要
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> allYearStat(Integer year, Integer channelCode, Integer month);
}
