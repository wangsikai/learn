package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.rescon.basedata.api.ResconTTSHandler;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.convert.ResconTextbookCategoryConvert;
import com.lanking.uxb.rescon.basedata.form.ResconTTSForm;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ResconTextbookCategoryHandler implements ResconTTSHandler {
	@Autowired
	@Qualifier("TextbookCategoryRepo")
	private Repo<TextbookCategory, Integer> repo;
	@Autowired
	private ResconTextbookCategoryConvert convert;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Override
	public ResconTTSType getType() {
		return ResconTTSType.TEXTBOOKCATEGORY;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(ResconTTSForm form) {
		TextbookCategory textbookCategory = new TextbookCategory();
		if (form.getId() == null) {
			TextbookCategory lastestCategory = repo.find("$findLastestCode", Params.param()).get();
			Integer code = 0;
			if (lastestCategory == null) {
				code = 11;
			} else {
				code = lastestCategory.getCode() + 1;
			}
			textbookCategory.setCode(code);
			textbookCategory.setSequence(form.getSequence());
			textbookCategory.setStatus(Status.ENABLED);
		} else {
			textbookCategory = repo.get(form.getId().intValue());
		}
		textbookCategory.setName(form.getName());
		repo.save(textbookCategory);
	}

	@Override
	public VResconTTS get(Long id) {
		TextbookCategory textbookCategory = repo.get(id.intValue());
		return convert.to(textbookCategory);
	}

	@Override
	public List<VResconTTS> findAll(Map<String, Object> params) {
		List<TextbookCategory> textbookCategories = repo.find("$findAllTextbookCategory", Params.param()).list();
		return convert.to(textbookCategories);
	}

	@Override
	public void syncData() {
		ClusterEvent<String> e = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.TEXTBOOKCATEGORY.name());
		sender.send(e);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateSequence(ResconTTSForm form) {
		TextbookCategory textbookCategory = repo.get(form.getId().intValue());
		textbookCategory.setSequence(form.getSequence());
		repo.save(textbookCategory);
	}

	@Override
	@Transactional
	public void updateSequence(List<ResconTTSForm> forms) {
		for (ResconTTSForm form : forms) {
			TextbookCategory category = repo.get(form.getId().intValue());
			category.setSequence(form.getSequence());
			repo.save(category);
		}
	}
}
