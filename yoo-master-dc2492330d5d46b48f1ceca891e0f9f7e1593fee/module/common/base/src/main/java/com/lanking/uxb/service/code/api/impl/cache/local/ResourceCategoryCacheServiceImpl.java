package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.ResourceCategoryService;

public class ResourceCategoryCacheServiceImpl extends AbstractBaseDataHandle implements ResourceCategoryService {

	@Override
	public List<ResourceCategory> getParentCategory() {
		return null;
	}

	@Override
	public ResourceCategory getResCategory(Integer resourceCategory) {
		return null;
	}

	@Override
	public List<ResourceCategory> findCategoryByParent(int code) {
		return null;
	}

	@Override
	public List<ResourceCategory> findAll() {
		return null;
	}

	@Override
	public List<ResourceCategory> getSubCategories() {
		return null;
	}

	@Override
	public Map<Integer, ResourceCategory> getcategories(Collection<Integer> keys) {
		return null;
	}

	@Override
	public ResourceCategory getResParentType(Integer key) {
		return null;
	}

	@Override
	public List<ResourceCategory> mgetList(Collection<Integer> ids) {
		return null;
	}

	@Override
	public Map<Integer, ResourceCategory> mget(Collection<Integer> ids) {
		return null;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.RESOURCE_CATEGORY;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {

	}

	@Override
	public long size() {
		return 0;
	}

}
