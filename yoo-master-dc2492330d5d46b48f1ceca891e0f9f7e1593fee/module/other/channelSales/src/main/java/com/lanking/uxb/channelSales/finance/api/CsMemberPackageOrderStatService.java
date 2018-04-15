package com.lanking.uxb.channelSales.finance.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface CsMemberPackageOrderStatService {

	/**
	 * 会员订单统计
	 * 
	 * @param query
	 * @return Page<Map>
	 */
	Page<Map> memberPackagerStat(CsMemberPackageOrderQuery query, Pageable pageable);

	/**
	 * 会员总订单金额
	 * 
	 * @param query
	 * @return List<Map>
	 */
	List<Map> memberPackagerSumStat(CsMemberPackageOrderQuery query);

	/**
	 * 查询当前有数据的年份(会员订单表)
	 * 
	 * @return
	 */
	List<Integer> getHasDataYearList();

	/**
	 * 根据渠道商进行财务统计
	 *
	 * @param query
	 *            {@link CsMemberPackageOrderQuery}
	 * @param pageable
	 *            {@link Pageable}
	 * @return {@link Page}
	 */
	Page<Map> queryMemberPackageStatByChannel(CsMemberPackageOrderQuery query, Pageable pageable);

}
