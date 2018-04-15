package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.cache.QuestionCarCacheService;
import com.lanking.uxb.service.zuoye.value.VQuestionCar;

@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ZyQuestionCarServiceImpl implements ZyQuestionCarService {

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	private QuestionCarCacheService qCarCacheService;

	@Override
	public List<Question> getQuestions(long userId) {
		Map<Long, Double> map = qCarCacheService.getAll(String.valueOf(userId));
		if (map == null || map.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		Params params = Params.param("ids", map.keySet());
		return questionRepo.find("$previewQuestions", params).list();
	}

	@Override
	public void addQuestion2Car(long userId, long id, double difficult, Question.Type type) {
		if (type != null) {
			qCarCacheService.addToCar(String.valueOf(userId), id, difficult, type.getValue());
		} else {
			qCarCacheService.addToCar(String.valueOf(userId), id, difficult, null);
		}
	}

	@Override
	public void removeFromCar(long userId, long id) {
		qCarCacheService.removeFromCar(String.valueOf(userId), id);
	}

	@Override
	public Map<Long, Double> getCarCondition(long userId) {
		return qCarCacheService.getAll(String.valueOf(userId));
	}

	@Override
	public List<VQuestionCar> mgetList(long userId) {
		return qCarCacheService.mgetList(String.valueOf(userId));
	}

	@Override
	public void removeAll(long userId) {
		qCarCacheService.removeAll(String.valueOf(userId));
	}

	@Override
	public List<Long> addQuestions2Car(long userId, List<Question> questionList) {
		List<Question> qList = Lists.newArrayList();
		for (Question question : questionList) {
			qList.add(question);
		}
		if (CollectionUtils.isNotEmpty(qList)) {
			for (Question question : qList) {
				this.addQuestion2Car(userId, question.getId(), question.getDifficulty(), question.getType());
			}
		}
		return qCarCacheService.mgetListIds(String.valueOf(userId));
	}

	@Override
	public Long countQuestions(long userId) {
		return qCarCacheService.countQuestions(String.valueOf(userId));
	}

	@Override
	public void addSortedQuestions(long userId, Collection<Long> questionIds) {
		qCarCacheService.addSortedQuestion(String.valueOf(userId), questionIds);
	}

	@Override
	public List<Long> getSortedQuestions(long userId) {
		return qCarCacheService.getSortedQuestionIds(String.valueOf(userId));
	}

	@Override
	public List<Long> getQuestionCarIds(long userId) {
		return qCarCacheService.mgetListIds(String.valueOf(userId));
	}

	@Override
	public void addQuestionIds(long userId, Collection<Long> questionIds) {
		qCarCacheService.addQuestionIds(String.valueOf(userId), questionIds);
	}

	@Override
	public List<Long> getQuestionIds(long userId) {
		return qCarCacheService.getQuestionIds(String.valueOf(userId));
	}

}
