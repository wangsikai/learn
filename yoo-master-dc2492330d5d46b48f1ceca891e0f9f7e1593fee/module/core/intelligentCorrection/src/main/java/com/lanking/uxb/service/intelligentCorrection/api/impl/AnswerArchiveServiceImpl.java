package com.lanking.uxb.service.intelligentCorrection.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.base.intelligentCorrection.AnswerArchive;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveService;
import com.lanking.uxb.service.intelligentCorrection.dao.AnswerArchiveDAO;

@Transactional(readOnly = true)
@Service
public class AnswerArchiveServiceImpl implements AnswerArchiveService {

	@Autowired
	private AnswerArchiveDAO answerArchiveDAO;

	@Transactional
	@Override
	public void createRightAnswer(long answerId, String content) {
		AnswerArchive aa = null;
		aa = answerArchiveDAO.find(answerId, content);
		if (aa == null) {
			aa = new AnswerArchive();
			aa.setAnswerId(answerId);
			aa.setContent(content);
		}
		aa.setResult(HomeworkAnswerResult.RIGHT);
		answerArchiveDAO.save(aa);
	}

	@Transactional
	@Override
	public void createWrongtAnswer(long answerId, String content) {
		AnswerArchive aa = null;
		aa = answerArchiveDAO.find(answerId, content);
		if (aa == null) {
			aa = new AnswerArchive();
			aa.setAnswerId(answerId);
			aa.setContent(content);
		}
		aa.setResult(HomeworkAnswerResult.WRONG);
		answerArchiveDAO.save(aa);
	}

	@Override
	public HomeworkAnswerResult getArchiveResult(long answerId, String content) {
		AnswerArchive answerArchive = answerArchiveDAO.find(answerId, content);
		return answerArchive == null ? null : answerArchive.getResult();
	}

	@Transactional
	@Override
	public void delete(long answerId) {
		answerArchiveDAO.delete(answerId, null, null);
	}

	@Transactional
	@Override
	public void delete(long answerId, String content) {
		answerArchiveDAO.delete(answerId, content, null);
	}

	@Transactional
	@Override
	public void deleteWrongAnswer(long answerId) {
		answerArchiveDAO.delete(answerId, null, HomeworkAnswerResult.WRONG);
	}

	@Transactional
	@Override
	public void deleteWrongAnswer(long answerId, String content) {
		answerArchiveDAO.delete(answerId, content, HomeworkAnswerResult.WRONG);

	}

	@Transactional
	@Override
	public void deleteRightAnswer(long answerId) {
		answerArchiveDAO.delete(answerId, null, HomeworkAnswerResult.RIGHT);
	}

	@Transactional
	@Override
	public void deleteRightAnswer(long answerId, String content) {
		answerArchiveDAO.delete(answerId, content, HomeworkAnswerResult.RIGHT);
	}

}
