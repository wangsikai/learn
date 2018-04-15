package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.cache.QuestionCarCacheService;
import com.lanking.uxb.service.zuoye.value.VQuestionCar;

@Transactional(readOnly = true)
public class ZyQuestionCarSessionServiceImpl implements ZyQuestionCarService {

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	private QuestionCarCacheService qCarCacheService;

	@Override
	public List<Question> getQuestions(long userId) {
		Map<Long, Double> map = qCarCacheService.getAll(Security.getToken());
		Params params = Params.param("ids", map.keySet());
		return questionRepo.find("$previewQuestions", params).list();
	}

	@Override
	public void addQuestion2Car(long userId, long id, double difficult, Question.Type type) {
		if (type != null) {
			qCarCacheService.addToCar(Security.getToken(), id, difficult, type.getValue());
		} else {
			qCarCacheService.addToCar(Security.getToken(), id, difficult, null);
		}
	}

	@Override
	public void removeFromCar(long userId, long id) {
		qCarCacheService.removeFromCar(Security.getToken(), id);
	}

	@Override
	public Map<Long, Double> getCarCondition(long userId) {
		return qCarCacheService.getAll(Security.getToken());
	}

	@Override
	public List<VQuestionCar> mgetList(long userId) {
		return qCarCacheService.mgetList(Security.getToken());
	}

	@Override
	public void removeAll(long userId) {
		qCarCacheService.removeAll(Security.getToken());
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
		return qCarCacheService.mgetListIds(Security.getToken());
	}

	@Override
	public Long countQuestions(long userId) {
		return qCarCacheService.countQuestions(Security.getToken());
	}

	@Override
	public void addSortedQuestions(long userId, Collection<Long> questionIds) {
		qCarCacheService.addSortedQuestion(Security.getToken(), questionIds);
	}

	@Override
	public List<Long> getSortedQuestions(long userId) {
		return qCarCacheService.getSortedQuestionIds(Security.getToken());
	}

	@Override
	public List<Long> getQuestionCarIds(long userId) {
		return qCarCacheService.mgetListIds(Security.getToken());
	}

	@Override
	public void addQuestionIds(long userId, Collection<Long> questionIds) {
		qCarCacheService.addQuestionIds(Security.getToken(), questionIds);

	}

	@Override
	public List<Long> getQuestionIds(long userId) {
		return qCarCacheService.getQuestionIds(Security.getToken());
	}

}
