package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;

/**
 * 商品快照相关接口.
 * 
 * @author wlche
 *
 */
public interface GoodsSnapshotService {
	GoodsSnapshot get(long id);

	Map<Long, GoodsSnapshot> mget(Collection<Long> ids);
}
