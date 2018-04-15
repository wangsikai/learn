package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgePointService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class KnowledgePointServiceImpl implements KnowledgePointService {
	@Autowired
	@Qualifier("KnowledgePointRepo")
	private Repo<KnowledgePoint, Long> pointRepo;

	@Override
	public KnowledgePoint get(Long code) {
		return pointRepo.get(code);
	}

	@Override
	public Map<Long, KnowledgePoint> mget(Collection<Long> codes) {
		return pointRepo.mget(codes);
	}

	@Override
	public List<KnowledgePoint> mgetList(Collection<Long> codes) {
		return pointRepo.mgetList(codes);
	}

	@Override
	public List<KnowledgePoint> findBySubject(Integer subjectCode) {
		return pointRepo.find("$findBySubject", Params.param("subjectCode", subjectCode)).list();
	}

	@Override
	public List<KnowledgePoint> findByPcode(Long pcode) {
		return pointRepo.find("$findByPcode", Params.param("pcode", pcode)).list();
	}

	@Override
	public List<KnowledgePoint> findAll(Collection<Long> noHasCodes) {
		Params params = Params.param();
		if (CollectionUtils.isNotEmpty(noHasCodes)) {
			params.put("noHasCodes", noHasCodes);
		}
		return pointRepo.find("$findAllKnowledgepoints", params).list();
	}
}
