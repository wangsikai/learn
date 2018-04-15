package com.lanking.uxb.service.code.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.MetaKnowKnow;
import com.lanking.cloud.domain.common.baseData.MetaKnowKnowKey;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.MetaKnowKnowService;

@Transactional(readOnly = true)
@Service
public class MetaKnowKnowServiceImpl implements MetaKnowKnowService {

	@Autowired
	@Qualifier("MetaKnowKnowRepo")
	Repo<MetaKnowKnow, MetaKnowKnowKey> metaKnowKnowRepo;

	@Transactional(readOnly = true)
	@Override
	public List<MetaKnowKnow> findByKnowpoint(int code) {
		return metaKnowKnowRepo.find("$findMetaKnowKnow", Params.param("knowPointCode", code)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<MetaKnowKnow> findByMetaKnowpoint(int code) {
		return metaKnowKnowRepo.find("$findMetaKnowKnow", Params.param("metaCode", code)).list();
	}

	@Override
	@Transactional(readOnly = false)
	public void save(MetaKnowKnow metaKnowKnow) {
		metaKnowKnowRepo.save(metaKnowKnow);
	}

}
