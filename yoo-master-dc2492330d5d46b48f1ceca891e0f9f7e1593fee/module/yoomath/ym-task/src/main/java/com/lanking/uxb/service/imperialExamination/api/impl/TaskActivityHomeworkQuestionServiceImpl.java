package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityHomeworkQuestionService;
import com.lanking.uxb.service.resources.ex.HomeworkException;

@Transactional(readOnly = true)
@Service
public class TaskActivityHomeworkQuestionServiceImpl implements TaskActivityHomeworkQuestionService {

	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	Repo<HomeworkQuestion, Long> homeworkQuestionRepo;

	@Override
	public List<Long> getQuestion(long homeworkId) throws HomeworkException {
		return homeworkQuestionRepo.find("$TaskGetQuestion", Params.param("homeworkId", homeworkId)).list(Long.class);
	}
}
