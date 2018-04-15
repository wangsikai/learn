package com.lanking.uxb.zycon.basedata.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.basedata.api.ZycResourceCategoryService;

/**
 * @see ZycResourceCategoryService
 * @author xinyu.zhou
 * @since V2.1
 */
@Transactional(readOnly = true)
@Service
public class ZycResourceCategoryServiceImpl implements ZycResourceCategoryService {

	@Autowired
	@Qualifier("ResourceCategoryRepo")
	private Repo<ResourceCategory, Integer> repo;

	@Override
	public List<ResourceCategory> findAll() {
		List<ResourceCategory> categories = repo.find("$findAllCategory", Params.param()).list();
		return categories;
	}
}
