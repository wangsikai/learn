package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsSnapshot;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.mall.api.CoinsLotteryGoodsSnapshotService;

/**
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Service
@Transactional(readOnly = true)
public class CoinsLotteryGoodsSnapshotServiceImpl implements CoinsLotteryGoodsSnapshotService {
	@Autowired
	@Qualifier("CoinsLotteryGoodsSnapshotRepo")
	private Repo<CoinsLotteryGoodsSnapshot, Long> repo;

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, CoinsLotteryGoodsSnapshot> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}
		return repo.mget(ids);
	}
}
