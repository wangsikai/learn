package com.lanking.uxb.service.intelligentCorrection.api;

import java.util.List;

public interface AnswerArchiveWrongLibrary2AnswerArchiveService {

	List<Long> queryAnswerId(long nextAnswerId, int fetchCount);

	void pushAnswerArchiveWrongLibrary2AnswerArchive(List<Long> answerIds);
}
