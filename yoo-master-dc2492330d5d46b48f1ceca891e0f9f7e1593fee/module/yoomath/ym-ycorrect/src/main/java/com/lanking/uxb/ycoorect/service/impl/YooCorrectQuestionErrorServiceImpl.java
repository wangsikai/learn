package com.lanking.uxb.ycoorect.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.resources.question.QuestionError;
import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionError;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.ycoorect.service.YooCorrectQuestionErrorService;

/**
 * 批改员错题反馈服务.
 * 
 * @author wanlong.che
 *
 */
@Service
public class YooCorrectQuestionErrorServiceImpl implements YooCorrectQuestionErrorService {
	@Autowired
	@Qualifier("CorrectQuestionErrorRepo")
	private Repo<CorrectQuestionError, Long> correctQuestionErrorRepo;

	@Autowired
	@Qualifier("QuestionErrorRepo")
	private Repo<QuestionError, Long> questionErrorRepo;

	@Transactional(readOnly = true)
	@Override
	public QuestionError get(long studentHomeworkQuestionId) {
		CorrectQuestionError correctQuestionError = correctQuestionErrorRepo.find("$getByStudentHomeworkQuetionId",
				Params.param("studentHomeorkQuestionId", studentHomeworkQuestionId)).get();
		if (correctQuestionError != null) {
			QuestionError questionError = questionErrorRepo.get(correctQuestionError.getQuestionErrorId());
			return questionError;
		}
		return null;
	}

	@Transactional
	@Override
	public void saveCorrectQuestionError(long studentHomeworkQuestionId, QuestionError questionError) {
		questionErrorRepo.save(questionError);
		CorrectQuestionError correctQuestionError = new CorrectQuestionError();
		correctQuestionError.setQuestionErrorId(questionError.getId());
		correctQuestionError.setQuestionId(questionError.getQuestionId());
		correctQuestionError.setStudentQuestionId(studentHomeworkQuestionId);
		correctQuestionError.setUserId(questionError.getUserId());
		correctQuestionError.setCreateAt(questionError.getCreateAt());
		correctQuestionErrorRepo.save(correctQuestionError);
	}
}
