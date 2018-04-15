package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;

@Transactional(readOnly = true)
@Service
public class ResourcesGoodsServiceImpl implements ResourcesGoodsService {

	@Autowired
	@Qualifier("ResourcesGoodsRepo")
	private Repo<ResourcesGoods, Long> repo;

	@Override
	public ResourcesGoods getGoodsByResourcesId(Long resourcesId) {
		return repo.find("$getGoodsByResourcesId", Params.param("resourcesId", resourcesId)).get();
	}

	@Override
	public List<ResourcesGoods> mgetGoods(Collection<Long> resourcesIds) {
		return repo.find("$mgetGoods", Params.param("resourcesIds", resourcesIds)).list();
	}

	@Override
	public ResourcesGoods get(Long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, ResourcesGoods> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

}
