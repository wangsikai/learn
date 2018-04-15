package com.lanking.uxb.zycon.mall.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.uxb.zycon.mall.api.ZycGoodsSnapshotService;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class ZycGoodsSnapshotServiceImpl implements ZycGoodsSnapshotService {

	@Autowired
	@Qualifier("GoodsSnapshotRepo")
	private Repo<GoodsSnapshot, Long> repo;

	@Override
	public GoodsSnapshot get(Long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, GoodsSnapshot> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

}
