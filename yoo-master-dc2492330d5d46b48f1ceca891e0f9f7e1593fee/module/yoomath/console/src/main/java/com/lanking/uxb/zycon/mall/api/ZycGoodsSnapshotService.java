package com.lanking.uxb.zycon.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;

/**
 * 金币商品快照service
 * 
 * @since V2.0.3
 * @author wangsenhao
 *
 */
public interface ZycGoodsSnapshotService {
	/**
	 * 获取单个商品快照
	 * 
	 * @param id
	 * @return
	 */
	GoodsSnapshot get(Long id);

	/**
	 * 批量获取商品快照
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, GoodsSnapshot> mget(Collection<Long> ids);

}
