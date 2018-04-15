package com.lanking.uxb.zycon.qs.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.qs.api.ZycQuestionSectionService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Service
@Transactional(readOnly = true)
public class ZycQuestionSectionServiceImpl implements ZycQuestionSectionService {

	@Autowired
	@Qualifier("QuestionSectionRepo")
	private Repo<QuestionSection, Long> repo;

	@Override
	public List<Integer> findByQuestionIds(Collection<Long> questionIds) {
		Params params = Params.param();
		params.put("questionIds", questionIds);
		List<QuestionSection> questionSections = repo.find("$zycFindByQuestionIds", params).list();
		List<Integer> ids = Lists.newArrayList();
		for (QuestionSection q : questionSections) {
			ids.add(q.getTextBookCode());
		}
		return ids;
	}
}
