package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsSnapshot;

/**
 * 金币商品snapshot service
 *
 * @author xinyu.zhou
 * @since 3.9.3
 */
public interface CoinsGoodsSnapshotService {

	/**
	 * mget
	 *
	 * @param ids
	 *            id列表
	 * @return {@link Map}
	 */
	Map<Long, CoinsGoodsSnapshot> mget(Collection<Long> ids);
}
