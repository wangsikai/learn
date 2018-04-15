package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;

/**
 * 抽奖相关Service
 *
 * @author xinyu.zhou
 * @since 2.0.4
 */
public interface CoinsLotteryGoodsService {

	/**
	 * 查找所有抽奖商品
	 *
	 * @return {@link List}
	 */
	List<CoinsLotteryGoods> findAll(long seasonId);

	/**
	 * 抽奖
	 * 
	 * @param userId
	 *            用户id
	 * @param id
	 *            查询期别id
	 */
	CoinsGoodsOrder lottery(long userId, Long id);

	/**
	 * 根据id查询数据
	 *
	 * @param id
	 *            抽奖奖品id
	 * @return {@link CoinsLotteryGoods}
	 */
	CoinsLotteryGoods get(long id);

	/**
	 * mget 抽奖商品
	 *
	 * @param ids
	 *            id列表
	 * @return {@link CoinsLotteryGoods}
	 */
	Map<Long, CoinsLotteryGoods> mget(Collection<Long> ids);

	/**
	 * 假期活动01抽奖
	 * 
	 * @param userId
	 *            用户id
	 * @param code
	 *            活动code
	 * @param drawSet
	 *            抽奖设置:0正常抽奖,1不可出奖,2达到保底设置
	 * @param
	 * 
	 */
	Map<String, Object> holidayDraw(long userId, long code, CoinsLotterySeason season, int drawSet);
}
