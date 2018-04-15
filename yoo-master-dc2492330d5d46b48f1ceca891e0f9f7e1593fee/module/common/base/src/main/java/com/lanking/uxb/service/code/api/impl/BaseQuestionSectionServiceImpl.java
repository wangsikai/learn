package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.BaseQuestionSectionService;

@Service
@Transactional(readOnly = true)
public class BaseQuestionSectionServiceImpl implements BaseQuestionSectionService {

	@Autowired
	@Qualifier("QuestionSectionRepo")
	private Repo<QuestionSection, Integer> repo;

	@Override
	@Transactional
	public void saveQuestionSections(long questionId, Collection<QuestionSection> questionSections) {
		repo.execute("delete from question_section where question_id =:questionId",
				Params.param("questionId", questionId));
		if (questionSections.size() > 0) {
			repo.save(questionSections);
		}
	}
}
