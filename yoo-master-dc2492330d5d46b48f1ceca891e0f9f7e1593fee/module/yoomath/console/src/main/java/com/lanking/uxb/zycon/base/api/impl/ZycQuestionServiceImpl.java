package com.lanking.uxb.zycon.base.api.impl;

import java.util.ArrayList;
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
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.base.api.ZycQuestionService;

@Transactional(readOnly = true)
@Service
public class ZycQuestionServiceImpl implements ZycQuestionService {

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("AnswerRepo")
	Repo<Answer, Long> answerRepo;

	@Transactional(readOnly = true)
	@Override
	public Question get(long id) {
		return questionRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, Question> mget(Collection<Long> ids) {
		return questionRepo.mget(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Question> mgetList(Collection<Long> ids) {
		return questionRepo.mgetList(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Question> getSubQuestions(long id) {
		return questionRepo.find("$getSubQuestions", Params.param("parentId", id)).list();
	}

	@Override
	public List<Question> findQuestionByCode(List<String> qCodes) {
		return questionRepo.find("$findQuestionByCode", Params.param("codes", qCodes)).list();
	}

	@Override
	public List<Long> findQuestionIdsByCode(List<String> qCodes, String isOrderDif) {
		Params params = Params.param();
		params.put("codes", qCodes);
		if (isOrderDif != null) {
			params.put("isOrderDif", isOrderDif);
		}
		return questionRepo.find("$findQuestionIdsByCode", params).list(Long.class);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, List<Question>> mgetSubQuestions(Collection<Long> ids) {
		Map<Long, List<Question>> map = new HashMap<Long, List<Question>>(ids.size());
		for (Long id : ids) {
			map.put(id, new ArrayList<Question>());
		}
		List<Question> list = questionRepo.find("$getSubQuestions", Params.param("parentIds", ids)).list();
		for (Question question : list) {
			map.get(question.getParentId()).add(question);
		}
		return map;
	}

	@Override
	public Map<String, Question> mgetByCode(Collection<String> codes) {
		List<Question> questions = questionRepo.find("$zycGetByCodes", Params.param("codes", codes)).list();
		Map<String, Question> map = Maps.newHashMap();
		for (Question q : questions) {
			map.put(q.getCode(), q);
		}
		return map;
	}

	@Transactional
	@Override
	public void updateSchool(long id, long schoolId) {
		questionRepo.execute("$zycUpdateSchool", Params.param("id", id).put("schoolId", schoolId));
	}

	@Override
	public void updateSchool(Collection<Long> ids, long schoolId) {
		questionRepo.execute("$zycUpdateSchool", Params.param("ids", ids).put("schoolId", schoolId));
	}

}
