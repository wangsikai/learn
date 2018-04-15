package com.lanking.uxb.service.intelligentCorrection.api;

public interface AnswerArchiveWrongLibraryService {

	void update(long answerId, String content);

	void delete(long answerId, String content);
}
