package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityQuestionService;

@Transactional(readOnly = true)
@Service
public class TaskActivityQuestionServiceImpl implements TaskActivityQuestionService {
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Override
	public boolean hasQuestionAnswering(Collection<Long> ids) {
		return questionRepo.find("$TaskHasQuestionAnswering", Params.param("ids", ids)).count() > 0;
	}

	@Override
	public List<Question> mgetList(Collection<Long> ids) {
		return questionRepo.mgetList(ids);
	}

	@Override
	public List<Question> getSubQuestions(long id) {
		return questionRepo.find("$TaskGetSubQuestions", Params.param("parentId", id)).list();
	}

	@Override
	public Map<Long, Question> mget(Collection<Long> ids) {
		return questionRepo.mget(ids);
	}
}
