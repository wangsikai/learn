package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledgeReview;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeReviewService;

@Transactional(readOnly = true)
@Service
public class ResconQuestionKnowledgeReviewServiceImpl implements ResconQuestionKnowledgeReviewService {
	@Autowired
	@Qualifier("QuestionKnowledgeReviewRepo")
	private Repo<QuestionKnowledgeReview, Long> repo;

	@Autowired
	@Qualifier("KnowledgeReviewRepo")
	private Repo<KnowledgeReview, Long> krRepo;

	@Override
	public List<KnowledgeReview> listByQuestion(Long questionId) {
		return repo.find("$resListKnowledgeReviewByQuestion", Params.param("questionId", questionId))
				.list(KnowledgeReview.class);
	}

	@Override
	public Map<Long, List<KnowledgeReview>> mListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledgeReview> qkrs = repo
				.find("$resListKnowledgeReviewByQuestions", Params.param("questionIds", questionIds)).list();
		Set<Long> knowledgeReviewCodes = new HashSet<Long>(qkrs.size());
		for (QuestionKnowledgeReview questionKnowledgeReview : qkrs) {
			knowledgeReviewCodes.add(questionKnowledgeReview.getKnowledgeCode());
		}
		if (knowledgeReviewCodes.size() > 0) {
			Map<Long, List<KnowledgeReview>> rmap = new HashMap<Long, List<KnowledgeReview>>(questionIds.size());
			Map<Long, KnowledgeReview> knowledgeReviewMap = krRepo.mget(knowledgeReviewCodes);
			for (QuestionKnowledgeReview questionKnowledgeReview : qkrs) {
				long questionId = questionKnowledgeReview.getQuestionId();
				long KnowledgeReviewCode = questionKnowledgeReview.getKnowledgeCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<KnowledgeReview>());
				}
				if (null != knowledgeReviewMap.get(KnowledgeReviewCode)) {
					rmap.get(questionId).add(knowledgeReviewMap.get(KnowledgeReviewCode));
				}
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	@Override
	@Transactional
	public void saveQuestionKnowledgeReview(long questionId, List<Long> knowledgeReviewIds) {
		repo.execute("delete from question_knowledge_review where question_id=:questionId",
				Params.param("questionId", questionId));
		repo.execute("delete from question_knowledge_sync where question_id=:questionId",
				Params.param("questionId", questionId));
		if (CollectionUtils.isEmpty(knowledgeReviewIds)) {
			return;
		}
		for (int i = 0; i < knowledgeReviewIds.size(); i++) {
			QuestionKnowledgeReview questionKnowledgeReview = new QuestionKnowledgeReview();
			questionKnowledgeReview.setQuestionId(questionId);
			questionKnowledgeReview.setKnowledgeCode(knowledgeReviewIds.get(i));
			repo.save(questionKnowledgeReview);
		}
	}
}
