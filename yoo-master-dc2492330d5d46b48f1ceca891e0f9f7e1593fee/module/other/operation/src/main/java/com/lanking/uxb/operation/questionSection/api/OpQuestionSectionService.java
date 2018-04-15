package com.lanking.uxb.operation.questionSection.api;

import java.util.List;

public interface OpQuestionSectionService {

	void convert(int textbookCode, List<Long> questionIds);
}
