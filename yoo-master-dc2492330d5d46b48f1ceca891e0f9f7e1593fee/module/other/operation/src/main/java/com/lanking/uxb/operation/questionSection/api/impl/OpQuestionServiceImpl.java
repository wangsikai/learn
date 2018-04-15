package com.lanking.uxb.operation.questionSection.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.operation.questionSection.api.OpQuestionService;

@Service
@Transactional(readOnly = true)
public class OpQuestionServiceImpl implements OpQuestionService {

	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> repo;

	@Override
	public CursorPage<Long, Long> query(int textbookCode, CursorPageable<Long> cursorPageable) {
		int phaseCode = textbookCode / 100000 % 10;
		int subjectCode = textbookCode / 100 % 1000;
		return repo.find("$opQuery", Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode)).fetch(
				cursorPageable, Long.class);
	}

	@Override
	public long remaining(int textbookCode, long id) {
		int phaseCode = textbookCode / 100000 % 10;
		int subjectCode = textbookCode / 100 % 1000;
		return repo.find("$opRemaining",
				Params.param("id", id).put("phaseCode", phaseCode).put("subjectCode", subjectCode)).count();
	}

}
