package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.rescon.basedata.api.ResconResourceCategoryService;
import com.lanking.uxb.rescon.basedata.form.ResconResourceCategoryForm;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * @see ResconResourceCategoryService
 * @author xinyu.zhou
 * @since V2.1
 */
@Transactional(readOnly = true)
@Service
public class ResconResourceCategoryServiceImpl implements ResconResourceCategoryService {
	@Autowired
	@Qualifier("ResourceCategoryRepo")
	private Repo<ResourceCategory, Integer> repo;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Override
	@Transactional(readOnly = false)
	public void save(ResconResourceCategoryForm form) {
		ResourceCategory resourceCategory = new ResourceCategory();
		if (form.getCode() == null) {
			Params params = Params.param("pcode", form.getPcode());
			ResourceCategory lastestCategory = repo.find("$findLastestCode", params).get();
			Integer code = 0;
			if (lastestCategory != null) {
				code = lastestCategory.getCode() + 1;
			} else {
				if (form.getPcode() == -1) {
					code = 1;
				} else {
					code = Integer.valueOf(form.getPcode() + "01");
				}
			}
			resourceCategory.setName(form.getName());
			resourceCategory.setCode(code);
			resourceCategory.setPcode(form.getPcode());
			resourceCategory.setSequence(form.getSequence());
		} else {
			resourceCategory = repo.get(form.getCode());
		}
		resourceCategory.setName(form.getName());
		repo.save(resourceCategory);
	}

	@Override
	public List<ResourceCategory> findAll() {
		List<ResourceCategory> categories = repo.find("$findAll", Params.param()).list();
		return categories;
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.RESOURCE_CATEGORY.name());
		sender.send(event);
	}

	@Override
	@Transactional
	public void updateSequence(int code, int sequence) {
		ResourceCategory category = repo.get(code);
		category.setSequence(sequence);

		repo.save(category);
	}

	@Override
	@Transactional
	public void updateSequence(List<ResconResourceCategoryForm> forms) {
		for (ResconResourceCategoryForm form : forms) {
			ResourceCategory resourceCategory = repo.get(form.getCode());
			resourceCategory.setSequence(form.getSequence());
			repo.save(resourceCategory);
		}
	}
}
