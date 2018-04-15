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
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.uxb.service.code.api.KnowledgeReviewService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class KnowledgeReviewServiceImpl implements KnowledgeReviewService {
	@Autowired
	@Qualifier("KnowledgeReviewRepo")
	private Repo<KnowledgeReview, Long> knowledgeReviewRepo;

	@Override
	public Map<Long, KnowledgeReview> mget(Collection<Long> codes) {
		return knowledgeReviewRepo.mget(codes);
	}

	@Override
	public List<KnowledgeReview> mgetList(Collection<Long> codes) {
		return knowledgeReviewRepo.mgetList(codes);
	}

}
