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
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.uxb.service.code.api.KnowledgeSyncService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class KnowledgeSyncServiceImpl implements KnowledgeSyncService {
	@Autowired
	@Qualifier("KnowledgeSyncRepo")
	private Repo<KnowledgeSync, Long> knowledgeSyncRepo;

	@Override
	public Map<Long, KnowledgeSync> mget(Collection<Long> codes) {
		return knowledgeSyncRepo.mget(codes);
	}

	@Override
	public List<KnowledgeSync> mgetList(Collection<Long> codes) {
		return knowledgeSyncRepo.mgetList(codes);
	}

}
