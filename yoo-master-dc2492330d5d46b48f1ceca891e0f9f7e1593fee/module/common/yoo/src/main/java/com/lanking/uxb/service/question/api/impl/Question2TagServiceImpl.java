package com.lanking.uxb.service.question.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.question.api.Question2TagService;

@Transactional(readOnly = true)
@Service
public class Question2TagServiceImpl implements Question2TagService {

	@Autowired
	@Qualifier("Question2TagRepo")
	private Repo<Question2Tag, Long> question2TagRepo;

	@Override
	public Question2Tag get(long id) {
		return question2TagRepo.get(id);
	}

	@Override
	public Map<Long, Question2Tag> mget(Collection<Long> ids) {
		return question2TagRepo.mget(ids);
	}

	@Override
	public List<Question2Tag> getByQuestionId(long questionId) {
		return question2TagRepo.find("$getByQuestionId", Params.param("questionId", questionId)).list();
	}

	@Override
	public Map<Long, List<Question2Tag>> mgetByQuestionIds(Collection<Long> questionIds) {
		List<Question2Tag> question2Tags = question2TagRepo
				.find("$mgetByQuestionIds", Params.param("questionIds", questionIds)).list();
		Map<Long, List<Question2Tag>> map = new HashMap<Long, List<Question2Tag>>();
		for (Question2Tag question2Tag : question2Tags) {
			List<Question2Tag> q2ts = map.get(question2Tag.getQuestionId());
			if (q2ts == null) {
				q2ts = new ArrayList<Question2Tag>();
			}
			q2ts.add(question2Tag);
		}
		return map;
	}
}
