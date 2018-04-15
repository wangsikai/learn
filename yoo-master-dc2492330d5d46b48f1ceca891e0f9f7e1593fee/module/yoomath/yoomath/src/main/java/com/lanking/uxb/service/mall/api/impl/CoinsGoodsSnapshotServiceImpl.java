package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsSnapshot;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.mall.api.CoinsGoodsSnapshotService;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
@Service
@Transactional(readOnly = true)
public class CoinsGoodsSnapshotServiceImpl implements CoinsGoodsSnapshotService {
	@Autowired
	@Qualifier("CoinsGoodsSnapshotRepo")
	private Repo<CoinsGoodsSnapshot, Long> repo;

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, CoinsGoodsSnapshot> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}
		return repo.mget(ids);
	}
}
