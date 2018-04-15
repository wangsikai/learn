package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationQuestion;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

public interface TaskImperialExaminationQuestionService {

	/**
	 * 根据code查询题目
	 * 
	 * @param code
	 *            活动代码
	 * @param type
	 * @return {@link ImperialExaminationQuestion}
	 */
	List<ImperialExaminationQuestion> get(long code, ImperialExaminationType type, 
			Integer room, Integer category, ImperialExaminationExamTag tag);
	
}
