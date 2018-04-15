package com.lanking.uxb.service.intelligentCorrection.api;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

public interface AnswerArchiveService {

	void createRightAnswer(long answerId, String content);

	void createWrongtAnswer(long answerId, String content);

	HomeworkAnswerResult getArchiveResult(long answerId, String content);

	void delete(long answerId);

	void delete(long answerId, String content);

	void deleteWrongAnswer(long answerId);

	void deleteWrongAnswer(long answerId, String content);

	void deleteRightAnswer(long answerId);

	void deleteRightAnswer(long answerId, String content);

}
