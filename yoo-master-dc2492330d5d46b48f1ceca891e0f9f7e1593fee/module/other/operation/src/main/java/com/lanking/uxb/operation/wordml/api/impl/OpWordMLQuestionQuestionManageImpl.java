package com.lanking.uxb.operation.wordml.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.operation.wordml.api.OpWordMLQuestionManage;

@Service
@Transactional(readOnly = true)
public class OpWordMLQuestionQuestionManageImpl implements OpWordMLQuestionManage {

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Override
	public Page<Question> wordMLQueryByPage(int type, Long minId, Pageable pageable) {
		Params params = Params.param();
		if (minId != null) {
			params.put("minId", minId);
		}
		if (type == 0) {
			return questionRepo.find("$buildWordMLAllQuery", params).fetch(pageable);
		} else {
			return questionRepo.find("$buildWordMLQuery", params).fetch(pageable);
		}
	}

}
