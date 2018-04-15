package com.lanking.uxb.channelSales.finance.api;

import java.util.List;
import java.util.Map;

public interface CsFinanceStatisticsService {

	/**
	 * 获取渠道商财务相关统计数据
	 * 
	 * @param query
	 * @return
	 */
	Map<String, Object> getChannelStat(CsFinanceQuery query);

	/**
	 * 获取会员卡财务相关统计数据<br>
	 * 注：目前版本会员卡只有学生会员，其他为0，后续会开通其他的
	 * 
	 * @param query
	 * @return
	 */
	Map<String, Object> getMemberCardStat(CsFinanceQuery query);

	/**
	 * 获取学生自主消费财务相关统计数据
	 * 
	 * @param query
	 * @return
	 */
	Map<String, Object> getStuAutoStat(CsFinanceQuery query);

	/**
	 * 获取老师自主消费财务相关统计数据
	 * 
	 * @param query
	 * @return
	 */
	Map<String, Object> getTeaAutoStat(CsFinanceQuery query);

	/**
	 * 查询会员相关统计
	 * 
	 * @param query
	 * @return
	 */
	Double queryBaseStatByCondition(CsFinanceQuery query);

	/**
	 * 根据是否渠道商用户，统计错题代打印金额
	 * 
	 * @param query
	 * @return
	 */
	Double queryFallPrintStat(CsFinanceQuery query);

	/**
	 * 查询购买试卷相关金额统计
	 * 
	 * @param query
	 * @return
	 */
	Double queryBuyPaperStat(CsFinanceQuery query);

	/**
	 * 
	 * @param query
	 * @return
	 */
	Map<String, Object> getStatMap(CsFinanceQuery query);

	/**
	 * 查询当前有数据的年份(会员订单表，错题本代印表，购买试卷订单表)
	 * 
	 * @return
	 */
	List<Integer> getHasDataYearList();
}
