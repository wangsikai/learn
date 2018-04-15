package com.lanking.uxb.service.mall.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoods;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsCategory;

public interface LotteryActivityGoodsService {

	LotteryActivityGoods get(long id);

	/**
	 * 根据活动ID获取所有的活动奖品数据.
	 * 
	 * @param activityCode
	 *            活动CODE
	 * @return
	 */
	List<LotteryActivityGoods> listByActivity(long activityCode);

	/**
	 * 获取活动的心灵鸡汤商品.
	 * 
	 * @param activityCode
	 * @return
	 */
	LotteryActivityGoods getNothingGoods(long activityCode);

	/**
	 * 增加活动奖品售卖（获奖）记录.
	 * 
	 * @param goodsId
	 */
	void incrGoodsSelled(long goodsId);

	/**
	 * 初始化2017年元旦抽奖活动商品数据.
	 * 
	 */
	List<LotteryActivityGoods> initNYD2017ActivityGoods(long activityCode, List<LotteryActivityGoodsCategory> categorys);
}