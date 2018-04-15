package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.Goods;

/**
 * 商品相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
public interface GoodsService {

	Goods get(long id);

	Map<Long, Goods> mget(Collection<Long> ids);

	/**
	 * 批量获得商品
	 *
	 * @param ids
	 *            商品id列表
	 * @return {@link List}
	 */
	List<Goods> mgetList(Collection<Long> ids);
}
