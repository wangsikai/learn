package com.lanking.uxb.service.resources.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.AppealType;
import com.lanking.cloud.domain.yoomath.homework.CorrectQuestionSource;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.intercomm.yoocorrect.service.CorrectQuestionDatawayService;
import com.lanking.uxb.service.correct.api.CorrectLogService;
import com.lanking.uxb.service.resources.api.QuestionAppealService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.session.api.impl.Security;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class QuestionAppealServiceImpl implements QuestionAppealService {

	@Autowired
	@Qualifier("QuestionAppealRepo")
	Repo<QuestionAppeal, Long> questionAppealRepo;

	@Autowired
	private StudentHomeworkAnswerService shaService;

	@Autowired
	private StudentHomeworkQuestionService shqService;

	@Autowired
	private CorrectLogService logService;

	@Autowired
	private CorrectQuestionDatawayService correctQuestionDatawayService;

	@Autowired
	private StudentHomeworkService shService;

	@Override
	@Transactional
	public void addComment(AppealType type, Long id, Integer source, String comment, UserType userType) {
		QuestionAppeal appeal = new QuestionAppeal();
		appeal.setBizId(id);
		appeal.setComment(comment);
		appeal.setCreator(Security.getUserId());
		appeal.setCreateAt(new Date());
		appeal.setUserType(userType);
		appeal.setStatus(QuestionAppealStatus.INIT);
		appeal.setType(type);
		appeal.setSource(CorrectQuestionSource.findByValue(source));

		StudentHomeworkQuestion question = shqService.get(id);
		if (question != null) {
			appeal.setAppealRightRate(question.getRightRate());
			appeal.setResult(question.getResult());
		}

		// 查询订正题
		List<StudentHomeworkQuestion> correctQuestions = shqService.queryStuQuestions(question.getStudentHomeworkId(),
				question.getQuestionId(), null, true);
		StudentHomeworkQuestion correctQuestion = null;
		if (CollectionUtils.isNotEmpty(correctQuestions)) {
			correctQuestion = correctQuestions.get(0);
			appeal.setCorrectAppealRightRate(correctQuestion.getRightRate());
			appeal.setCorrectResult(correctQuestion.getResult());
		}

		// 查找一条最新的批改记录，获取批改人id和批改方式
		QuestionCorrectLog log = logService.getNewestLog(id);

		if (log != null) {
			appeal.setAppealCorrectUserId(log.getUserId());
			appeal.setCorrectType(log.getCorrectType());
		}

		List<StudentHomeworkAnswer> answers = shaService.find(id);
		if (CollectionUtils.isNotEmpty(answers)) {
			for (StudentHomeworkAnswer answer : answers) {
				appeal.getItemResults().add(answer.getResult());
			}
		}

		if (correctQuestion != null) {
			List<StudentHomeworkAnswer> correctAnswers = shaService.find(correctQuestion.getId());
			if (CollectionUtils.isNotEmpty(correctAnswers)) {
				for (StudentHomeworkAnswer answer : correctAnswers) {
					appeal.getCorrectItemResults().add(answer.getResult());
				}
			}
		}

		questionAppealRepo.save(appeal);

		// 申诉题发送至小优快批
		StudentHomework sh = shService.get(question.getStudentHomeworkId());
		correctQuestionDatawayService.sendCorrectQuestion(sh.getStudentId(), Lists.newArrayList(question.getId()),
				true);
	}

	@Override
	public QuestionAppeal getAppeal(Long sHkQId) {
		Params param = Params.param("sHkQId", sHkQId);
		QuestionAppeal appeal = questionAppealRepo.find("$findQuestionAppeal", param).get();

		return appeal;
	}

	@Override
	@Transactional
	public void updateStatus(Long id, QuestionAppealStatus status) {
		Params param = Params.param("id", id);
		param.put("status", status.getValue());

		questionAppealRepo.execute("$updateAppealStatus", param);
	}

	@Override
	@Transactional(readOnly = true)
	public QuestionAppeal getLastAppeal(long sHkQId) {
		Params params = Params.param("sHkQId", sHkQId);
		return questionAppealRepo.find("$getLastAppeal", params).get();
	}
}
