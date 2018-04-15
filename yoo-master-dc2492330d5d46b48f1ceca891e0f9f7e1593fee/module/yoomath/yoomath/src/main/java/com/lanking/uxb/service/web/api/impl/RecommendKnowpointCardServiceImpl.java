package com.lanking.uxb.service.web.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.recommend.RecommendKnowpointCard;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.web.api.DailyPractiseGenerateService;
import com.lanking.uxb.service.web.api.RecommendKnowpointCardService;

/**
 * @see DailyPractiseGenerateService
 * @author xinyu.zhou
 * @since 2.0.3
 */
@Service
@Transactional(readOnly = true)
public class RecommendKnowpointCardServiceImpl implements RecommendKnowpointCardService {
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;

	@Autowired
	@Qualifier("RecommendKnowpointCardRepo")
	private Repo<RecommendKnowpointCard, Long> repo;

	@Override
	public RecommendKnowpointCard getRecommendKnowpointCard(Long studentId) {
		return repo.find("$zyGetRecommendCard", Params.param("userId", studentId)).get();
	}

	@Override
	public RecommendKnowpointCard getLastRecommendKnowpointCard(Long studentId) {
		return repo.find("$zyGetLastRecommendCard", Params.param("userId", studentId)).get();
	}
}
