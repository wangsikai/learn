package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.ResourceCategoryService;

@Service
@Transactional(readOnly = true)
public class ResourceCategoryServiceImpl implements ResourceCategoryService {

	@Autowired
	@Qualifier("ResourceCategoryRepo")
	private Repo<ResourceCategory, Integer> resourceCategoryRepo;

	@Override
	public List<ResourceCategory> getParentCategory() {
		return resourceCategoryRepo.find("$getParentCategory").list();
	}

	@Override
	public ResourceCategory getResCategory(Integer resourceCategory) {
		return resourceCategoryRepo.get(resourceCategory);
	}

	@Override
	public List<ResourceCategory> findCategoryByParent(int code) {
		return resourceCategoryRepo.find("$findCategoryByParent", Params.param("code", code)).list();
	}

	@Override
	public List<ResourceCategory> findAll() {
		return resourceCategoryRepo.getAll();
	}

	@Override
	public List<ResourceCategory> getSubCategories() {
		return resourceCategoryRepo.find("$getSubCategories").list();
	}

	@Override
	public Map<Integer, ResourceCategory> getcategories(Collection<Integer> keys) {
		return resourceCategoryRepo.mget(keys);
	}

	@Override
	public ResourceCategory getResParentType(Integer key) {
		return resourceCategoryRepo.find("$getResParentType", Params.param("code", key)).get();
	}

	@Override
	public List<ResourceCategory> mgetList(Collection<Integer> ids) {
		return resourceCategoryRepo.mgetList(ids);
	}

	@Override
	public Map<Integer, ResourceCategory> mget(Collection<Integer> ids) {
		return resourceCategoryRepo.mget(ids);
	}
}
