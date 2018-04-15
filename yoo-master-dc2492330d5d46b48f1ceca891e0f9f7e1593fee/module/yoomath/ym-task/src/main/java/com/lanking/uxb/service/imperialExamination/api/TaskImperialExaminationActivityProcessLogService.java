package com.lanking.uxb.service.imperialExamination.api;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityProcessLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationQuestion;

public interface TaskImperialExaminationActivityProcessLogService {

	/**
	 * 查询阶段日志
	 * 
	 * @param code
	 *            活动代码
	 * @param process
	 * @return {@link ImperialExaminationQuestion}
	 */
	ImperialExaminationActivityProcessLog get(long code, ImperialExaminationProcess process, String data);

	/**
	 * 创建日志
	 * 
	 * @param logs
	 *            活动代码
	 * @return
	 */
	void create(ImperialExaminationActivityProcessLog logs);

}
