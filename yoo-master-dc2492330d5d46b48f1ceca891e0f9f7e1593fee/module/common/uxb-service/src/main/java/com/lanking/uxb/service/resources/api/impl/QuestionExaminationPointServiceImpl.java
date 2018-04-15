package com.lanking.uxb.service.resources.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.resources.api.QuestionExaminationPointService;

/**
 * 题目与考点对应相关接口
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
public class QuestionExaminationPointServiceImpl implements QuestionExaminationPointService {
	@Autowired
	@Qualifier("QuestionExaminationPointRepo")
	private Repo<QuestionExaminationPoint, Long> repo;

	@Override
	@SuppressWarnings("unchecked")
	public List<QuestionExaminationPoint> findByQuestions(Collection<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return Collections.EMPTY_LIST;
		}
		return repo.find("$findByQuestions", Params.param("questionIds", questionIds)).list();
	}

	@Override
	public List<QuestionExaminationPoint> findByQuestion(long questionId) {
		return repo.find("$findByQuestion", Params.param("questionId", questionId)).list();
	}
}
