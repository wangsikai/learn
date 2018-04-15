package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsSnapshot;

/**
 * 奖品商品快照接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
public interface LotteryActivityGoodsSnapshotService {

	LotteryActivityGoodsSnapshot get(long snapshotId);

	Map<Long, LotteryActivityGoodsSnapshot> mget(Collection<Long> snapshotIds);
}
