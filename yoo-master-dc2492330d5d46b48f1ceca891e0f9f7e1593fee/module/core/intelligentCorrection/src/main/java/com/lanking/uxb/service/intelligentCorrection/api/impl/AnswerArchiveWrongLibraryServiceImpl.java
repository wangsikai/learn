package com.lanking.uxb.service.intelligentCorrection.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.base.intelligentCorrection.AnswerArchiveWrongLibrary;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveWrongLibraryService;
import com.lanking.uxb.service.intelligentCorrection.dao.AnswerArchiveWrongLibraryDAO;

@Transactional(readOnly = true)
@Service
public class AnswerArchiveWrongLibraryServiceImpl implements AnswerArchiveWrongLibraryService {

	@Autowired
	private AnswerArchiveWrongLibraryDAO answerArchiveWrongLibraryDAO;

	@Transactional
	@Override
	public void update(long answerId, String content) {
		int uptRows = answerArchiveWrongLibraryDAO.update(answerId, content);
		if (uptRows == 0) {
			AnswerArchiveWrongLibrary aawl = new AnswerArchiveWrongLibrary();
			aawl.setAnswerId(answerId);
			aawl.setContent(content);
			aawl.setStatus(Status.ENABLED);
			aawl.setTimes(1);
			answerArchiveWrongLibraryDAO.save(aawl);
		}

	}

	@Transactional
	@Override
	public void delete(long answerId, String content) {
		answerArchiveWrongLibraryDAO.delete(answerId, content);
	}
}
