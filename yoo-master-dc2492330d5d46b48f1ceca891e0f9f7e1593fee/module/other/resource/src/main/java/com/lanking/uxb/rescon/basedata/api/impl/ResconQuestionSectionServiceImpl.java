package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionSectionService;

@Transactional(readOnly = true)
@Service
public class ResconQuestionSectionServiceImpl implements ResconQuestionSectionService {

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

	@Override
	@Transactional
	public void saveQuestionSections(Collection<QuestionSection> questionSections) {
		if (questionSections.size() > 0) {
			Set<Long> questionIds = new HashSet<Long>(questionSections.size());
			for (QuestionSection questionSection : questionSections) {
				questionIds.add(questionSection.getQuestionId());
			}
			repo.execute("delete from question_section where question_id in (:questionIds)",
					Params.param("questionIds", questionIds));
			repo.save(questionSections);
		}
	}
}
