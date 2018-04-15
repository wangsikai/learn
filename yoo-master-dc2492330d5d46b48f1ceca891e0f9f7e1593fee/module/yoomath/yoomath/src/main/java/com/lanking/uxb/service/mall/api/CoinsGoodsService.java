package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;

/**
 * 金币商品相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
public interface CoinsGoodsService {

	CoinsGoods get(long id);

	Map<Long, CoinsGoods> mget(Collection<Long> ids);

	/**
	 * 查询所有商品(金币商城首页使用)
	 * 
	 * @since 2.0.3
	 * @param query
	 *            查询条件
	 * @return CoinsGoods Map
	 */
	Map<CoinsGoodsType, List<CoinsGoods>> listAllByType(CoinsGoodsQuery query);

	/**
	 * 根据coins group来查询组下面所有商品
	 *
	 * @param query
	 *            {@link CoinsGoodsQuery}
	 * @param groupIds
	 *            金币商品组id
	 * @return 组id->组下所有商品列表
	 */
	Map<Long, List<CoinsGoods>> listAllByGroup(CoinsGoodsQuery query, Collection<Long> groupIds);
}
