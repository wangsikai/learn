package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsSnapshot;
import com.lanking.uxb.service.mall.api.ResourcesGoodsSnapshotService;

@Service
@Transactional(readOnly = true)
public class ResourcesGoodsSnapshotServiceImpl implements ResourcesGoodsSnapshotService {
	@Autowired
	@Qualifier("ResourcesGoodsSnapshotRepo")
	private Repo<ResourcesGoodsSnapshot, Long> repo;

	@Override
	public ResourcesGoodsSnapshot get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, ResourcesGoodsSnapshot> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}
}
