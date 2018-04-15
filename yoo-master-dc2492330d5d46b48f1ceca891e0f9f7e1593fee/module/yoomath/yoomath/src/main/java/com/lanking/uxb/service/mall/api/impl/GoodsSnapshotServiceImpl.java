package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;

@Transactional(readOnly = true)
@Service
public class GoodsSnapshotServiceImpl implements GoodsSnapshotService {

	@Autowired
	@Qualifier("GoodsSnapshotRepo")
	private Repo<GoodsSnapshot, Long> goodsSnapshotRepo;

	@Override
	public GoodsSnapshot get(long id) {
		return goodsSnapshotRepo.get(id);
	}

	@Override
	public Map<Long, GoodsSnapshot> mget(Collection<Long> ids) {
		return goodsSnapshotRepo.mget(ids);
	}
}
