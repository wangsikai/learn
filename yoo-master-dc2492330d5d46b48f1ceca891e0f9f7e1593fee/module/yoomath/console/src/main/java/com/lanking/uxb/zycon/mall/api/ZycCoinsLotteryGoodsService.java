package com.lanking.uxb.zycon.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.uxb.zycon.mall.form.LotterySeasonForm;

/**
 * 抽奖商品
 * 
 * @author wangsenhao
 *
 */
public interface ZycCoinsLotteryGoodsService {
	/**
	 * 保存抽奖坑位等设置
	 * 
	 * @param form
	 */
	void saveLotteryGoods(LotterySeasonForm form);

	/**
	 * 
	 * @param id
	 * @return
	 */
	CoinsLotteryGoods get(Long id);

	/**
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, CoinsLotteryGoods> mget(Collection<Long> ids);

	/**
	 * 获取当前的活动Code
	 * 
	 * @return
	 */
	Integer getActiveCode();

}
