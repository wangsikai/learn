package com.lanking.uxb.zycon.mall.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoods;

public interface ZycCoinsLotterySeasonGoodsService {
	/**
	 * 通过抽奖商品id、期别id获取
	 * 
	 * @param coinsLotteryGoodsId
	 *            抽奖商品id
	 * @param seasonId
	 *            期别id
	 * @return
	 */
	CoinsLotterySeasonGoods getByKey(Long coinsLotteryGoodsId, Long seasonId);

	/**
	 * 通过seasonId查询对应商品
	 * 
	 * @param seasonId
	 * @return
	 */
	List<CoinsLotterySeasonGoods> findBySeasonId(Long seasonId);
}
