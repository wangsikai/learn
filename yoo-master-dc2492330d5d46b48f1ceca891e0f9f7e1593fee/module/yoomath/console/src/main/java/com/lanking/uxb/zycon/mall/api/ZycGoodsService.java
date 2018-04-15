package com.lanking.uxb.zycon.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.Goods;

/**
 * 金币商品service
 * 
 * @since V2.0
 * @author wangsenhao
 *
 */
public interface ZycGoodsService {
	/**
	 * 获取单个商品
	 * 
	 * @param id
	 * @return
	 */
	Goods get(Long id);

	/**
	 * 批量获取商品
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, Goods> mget(Collection<Long> ids);

}
