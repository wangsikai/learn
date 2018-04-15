package com.lanking.uxb.service.mall.api;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;

/**
 * 抽奖规则Service
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
public interface CoinsLotterySeasonService {

	/**
	 * 获取最新的规则说明
	 *
	 * @return {@link CoinsLotterySeason}
	 */
	CoinsLotterySeason findNewest();

	/**
	 * 更新获得的金币
	 *
	 * @param id
	 *            season id
	 * @param earnCoins
	 *            净收入
	 */
	void updateEarnCoins(Long id, Integer earnCoins);

	/**
	 * 根据id获得一期抽奖数据
	 *
	 * @param id
	 *            期别id
	 * @return {@link CoinsLotterySeason}
	 */
	CoinsLotterySeason get(long id);

	/**
	 * 根据活动code查找最新一期数据
	 *
	 * @param code
	 *            活动code
	 * @return {@link CoinsLotterySeason}
	 */
	CoinsLotterySeason findNewestByCode(Integer code);
}
