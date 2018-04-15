package com.lanking.uxb.operation.questionSection.api;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface OpQuestionService {

	CursorPage<Long, Long> query(int textbookCode, CursorPageable<Long> cursorPageable);

	long remaining(int textbookCode, long id);
}
