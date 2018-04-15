package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsDaySellCount;

/**
 * 金币商品每日销售统计
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
public interface CoinsGoodsDaySellCountService {

	/**
	 * 金币商品兑换数量更新
	 * 
	 * @since 2.0.3
	 * @param coinsGoodsId
	 *            金币商品ID
	 * @param date0
	 *            日期
	 * @param delta
	 *            增加的数量
	 * @param max
	 *            商品限购数量
	 * @return =1表示成功
	 */
	int incrCount(long coinsGoodsId, long date0, int delta, int max);

	CoinsGoodsDaySellCount get(long date0, long coinsGoodsId);

	Map<Long, CoinsGoodsDaySellCount> mget(long date0, Collection<Long> coinsGoodsIds);

}
