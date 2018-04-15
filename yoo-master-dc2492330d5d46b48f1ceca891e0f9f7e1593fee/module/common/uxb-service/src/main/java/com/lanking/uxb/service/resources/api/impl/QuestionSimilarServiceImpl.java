package com.lanking.uxb.service.resources.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.resources.api.QuestionSimilarService;

@Service
@Transactional(readOnly = true)
public class QuestionSimilarServiceImpl implements QuestionSimilarService {
	@Autowired
	@Qualifier("QuestionSimilarRepo")
	Repo<QuestionSimilar, Long> questionSimilarRepo;

	@Override
	public QuestionSimilar getByQuestion(long questionId) {
		return questionSimilarRepo.find("$querySimilarQuestionByBaseId", Params.param("baseQuestionId", questionId))
				.get();
	}

	@Override
	public Map<Long, QuestionSimilar> mGetByQuestion(Collection<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return Maps.newHashMap();
		}
		List<QuestionSimilar> lists = questionSimilarRepo
				.find("$querySimilarQuestionByBaseIds", Params.param("baseQuestionIds", questionIds)).list();
		Map<Long, QuestionSimilar> map = new HashMap<Long, QuestionSimilar>(lists.size());
		for (QuestionSimilar qs : lists) {
			map.put(qs.getBaseQuestionId(), qs);
		}
		return map;
	}
}
