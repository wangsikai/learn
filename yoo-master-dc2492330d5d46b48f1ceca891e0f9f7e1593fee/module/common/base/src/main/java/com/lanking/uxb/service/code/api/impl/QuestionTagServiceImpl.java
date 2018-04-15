package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.uxb.service.code.api.QuestionTagService;

@Transactional(readOnly = true)
@Service
public class QuestionTagServiceImpl implements QuestionTagService {

	@Autowired
	@Qualifier("QuestionTagRepo")
	private Repo<QuestionTag, Long> questionTagRepo;

	@Override
	public List<QuestionTag> getQuestionTag(Collection<Long> ids) {
		List<QuestionTag> resultList = Lists.newArrayList();
		Map<Long, QuestionTag> map = questionTagRepo.mget(ids);
		QuestionTag questionTag = null;
		for (Long tag : map.keySet()) {
			questionTag = map.get(tag);
			resultList.add(questionTag);
		}

		// 按照sequence排序
		Collections.sort(resultList, new Comparator<QuestionTag>() {
			@Override
			public int compare(QuestionTag q1, QuestionTag q2) {
				if (q1.getSequence().intValue() < q2.getSequence().intValue()) {
					return -1;
				} else if (q1.getSequence().intValue() > q2.getSequence().intValue()) {
					return 1;
				}
				return 0;
			}
		});

		return resultList;
	}

	@Override
	public Map<Long, QuestionTag> mget(Collection<Long> ids) {
		return questionTagRepo.mget(ids);
	}
}
