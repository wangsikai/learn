package com.lanking.uxb.service.intelligentCorrection.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.intelligentCorrection.AnswerArchiveWrongLibrary;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveService;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveWrongLibrary2AnswerArchiveService;

@Transactional(readOnly = true)
@Service
public class AnswerArchiveWrongLibrary2AnswerArchiveServiceImpl implements
		AnswerArchiveWrongLibrary2AnswerArchiveService {

	@Autowired
	@Qualifier("AnswerArchiveWrongLibraryRepo")
	Repo<AnswerArchiveWrongLibrary, Long> aawlRepo;

	@Autowired
	private AnswerArchiveService answerArchiveService;

	@Override
	public List<Long> queryAnswerId(long nextAnswerId, int fetchCount) {
		return aawlRepo.find("$taskQueryAnswerId",
				Params.param("nextAnswerId", nextAnswerId).put("fetchCount", fetchCount)).list(Long.class);
	}

	@Transactional
	@Override
	public void pushAnswerArchiveWrongLibrary2AnswerArchive(List<Long> answerIds) {
		for (Long answerId : answerIds) {
			answerArchiveService.deleteWrongAnswer(answerId);
			List<AnswerArchiveWrongLibrary> aawls = aawlRepo.find("$taskQueryWrongAnswer",
					Params.param("answerId", answerId)).list();
			for (AnswerArchiveWrongLibrary aawl : aawls) {
				answerArchiveService.createWrongtAnswer(answerId, aawl.getContent());
			}
		}
	}
}
