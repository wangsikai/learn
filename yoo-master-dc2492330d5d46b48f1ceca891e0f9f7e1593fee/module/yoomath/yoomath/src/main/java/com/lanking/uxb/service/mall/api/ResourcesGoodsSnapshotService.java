package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsSnapshot;

/**
 * 资源商品快照接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月6日
 */
public interface ResourcesGoodsSnapshotService {

	ResourcesGoodsSnapshot get(long id);

	Map<Long, ResourcesGoodsSnapshot> mget(Collection<Long> ids);
}
