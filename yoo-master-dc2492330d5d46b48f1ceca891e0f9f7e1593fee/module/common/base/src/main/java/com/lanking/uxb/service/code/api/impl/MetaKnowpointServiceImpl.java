package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.MetaKnowpointService;

@Transactional(readOnly = true)
@Service
public class MetaKnowpointServiceImpl implements MetaKnowpointService {

	@Autowired
	@Qualifier("MetaKnowpointRepo")
	Repo<MetaKnowpoint, Integer> mknowpointRepo;

	@Override
	public MetaKnowpoint get(Integer code) {
		return mknowpointRepo.get(code);
	}

	@Override
	public Map<Integer, MetaKnowpoint> mget(Collection<Integer> codes) {
		return mknowpointRepo.mget(codes);
	}

	@Override
	public List<MetaKnowpoint> mgetList(Collection<Integer> codes) {
		return mknowpointRepo.mgetList(codes);
	}

	@Override
	public List<MetaKnowpoint> listBySubject(int subjectCode, String key) {
		Params params = Params.param("subjectCode", subjectCode);
		if (StringUtils.isNotBlank(key)) {
			params.put("key", key);
		}
		return mknowpointRepo.find("$listBySubject", params).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> listBySubject2(int subjectCode) {
		Params params = Params.param("subjectCode", subjectCode);
		return mknowpointRepo.find("$listBySubject2", params).list(Map.class);
	}

	@Override
	public List<MetaKnowpoint> listByKnowPoint(Integer knowPointCode) {
		Params params = Params.param("knowPointCode", knowPointCode);
		return mknowpointRepo.find("$listByKnowpoint", params).list();
	}
}
