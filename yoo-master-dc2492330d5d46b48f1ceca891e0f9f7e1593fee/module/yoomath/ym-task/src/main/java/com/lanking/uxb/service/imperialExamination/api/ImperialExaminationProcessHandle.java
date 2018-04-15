package com.lanking.uxb.service.imperialExamination.api;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;

public interface ImperialExaminationProcessHandle {

	ImperialExaminationProcess getProcess();

	/**
	 * 当前阶段的试卷是否已下发
	 * 
	 * @param code
	 *            活动代码
	 * @return true|false
	 */
	boolean isPublished(long code);
	
	/**
	 * 当前阶段的奖券是否已处理
	 * 
	 * @param code
	 *            活动代码
	 * @return true|false
	 */
	boolean isLotteryProcessed(long code);

	/**
	 * 处理当前阶段逻辑
	 * 
	 * @param code
	 *            活动代码
	 */
	void process(long code);
}
