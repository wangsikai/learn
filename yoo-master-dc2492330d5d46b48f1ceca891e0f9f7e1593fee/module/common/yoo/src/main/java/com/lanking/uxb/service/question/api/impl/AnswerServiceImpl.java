package com.lanking.uxb.service.question.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.question.api.AnswerService;

@Transactional(readOnly = true)
@Service
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	@Qualifier("AnswerRepo")
	Repo<Answer, Long> answerRepo;

	@Transactional(readOnly = true)
	@Override
	public List<Answer> getQuestionAnswers(long questionId) {
		return answerRepo.find("$getQuestionAnswers", Params.param("qid", questionId)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, List<Answer>> getQuestionAnswers(Collection<Long> questionIds) {
		Map<Long, List<Answer>> map = Maps.newHashMap();
		for (Long qid : questionIds) {
			map.put(qid, Lists.<Answer>newArrayList());
		}
		List<Answer> answers = answerRepo.find("$getQuestionAnswers", Params.param("qids", questionIds)).list();
		for (Answer answer : answers) {
			List<Answer> as = map.get(answer.getQuestionId());
			as.add(answer);
			map.put(answer.getQuestionId(), as);
		}
		return map;
	}
}
