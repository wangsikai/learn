package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.resources.question.QuestionError;
import com.lanking.cloud.domain.support.resources.question.QuestionErrorType;
import com.lanking.uxb.service.zuoye.api.ZyQuestionErrorService;

@Transactional(readOnly = true)
@Service
public class ZyQuestionErrorServiceImpl implements ZyQuestionErrorService {

	@Autowired
	@Qualifier("QuestionErrorRepo")
	Repo<QuestionError, Long> questionErrorRepo;

	@Transactional
	@Override
	public void saveError(String description, List<QuestionErrorType> types, Long questionId, Long userId) {
		QuestionError qe = new QuestionError();
		qe.setCreateAt(new Date());
		if (description != null) {
			qe.setDescription(description);
		}
		qe.setQuestionId(questionId);
		if (types != null) {
			qe.setTypeList(types);
		}
		qe.setUserId(userId);
		questionErrorRepo.save(qe);
	}
}
