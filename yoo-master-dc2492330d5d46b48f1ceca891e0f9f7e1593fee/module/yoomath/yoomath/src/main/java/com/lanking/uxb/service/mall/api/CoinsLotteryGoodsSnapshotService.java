package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsSnapshot;

/**
 * 抽奖商品快照service
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
public interface CoinsLotteryGoodsSnapshotService {
	/**
	 * mget抽奖商品快照map
	 *
	 * @param ids
	 *            商品快照id列表
	 * @return {@link Map}
	 */
	Map<Long, CoinsLotteryGoodsSnapshot> mget(Collection<Long> ids);
}
