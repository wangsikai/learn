package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.MetaKnowpointMap;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyMetaKnowpointMapService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional(readOnly = true)
public class ZyMetaKnowpointMapServiceImpl implements ZyMetaKnowpointMapService {
	@Autowired
	@Qualifier("MetaKnowpointMapRepo")
	Repo<MetaKnowpointMap, Long> repo;

	@Override
	public List<MetaKnowpointMap> findByParent(int code) {
		return repo.find("$findByParent", Params.param("code", code)).list();
	}

	@Override
	public List<MetaKnowpointMap> findByParents(Collection<Integer> codes) {
		return repo.find("$findByParents", Params.param("codes", codes)).list();
	}
}
