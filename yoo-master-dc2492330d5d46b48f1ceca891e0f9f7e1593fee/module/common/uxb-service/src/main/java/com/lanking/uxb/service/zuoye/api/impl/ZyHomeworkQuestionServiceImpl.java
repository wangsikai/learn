package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuestionService;

@Transactional(readOnly = true)
@Service
public class ZyHomeworkQuestionServiceImpl implements ZyHomeworkQuestionService {

	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	Repo<HomeworkQuestion, Long> hkQuestionRepo;

	@Override
	public List<HomeworkQuestion> findByHomework(Collection<Long> homeworkIds) {
		return hkQuestionRepo.find("$zyFindByHomework", Params.param("homeworkIds", homeworkIds)).list();
	}

	@Override
	public List<Double> getHkQuestion(Long homeworkId) {
		return hkQuestionRepo.find("$getClazzQuestionStat", Params.param("homeworkId", homeworkId)).list(Double.class);
	}

	@Override
	public List<Map> listWrongStu(long homeworkId, long questionId) {
		return hkQuestionRepo
				.find("$listWrongStu", Params.param("homeworkId", homeworkId).put("questionId", questionId))
				.list(Map.class);
	}

	@Override
	public List<HomeworkQuestion> findCorrectQuestions(long homeworkId) {
		return hkQuestionRepo.find("$queryNeedCorrectQuestions", Params.param("homeworkId", homeworkId)).list();
	}

	@Override
	public List<Question> findQuestionByHomework(long homeworkId) {
		return hkQuestionRepo.find("$findQuestionByHomework", Params.param("homeworkId", homeworkId))
				.list(Question.class);
	}
}
