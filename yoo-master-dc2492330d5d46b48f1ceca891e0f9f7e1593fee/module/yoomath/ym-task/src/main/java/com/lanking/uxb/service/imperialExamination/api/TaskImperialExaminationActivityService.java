package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;

public interface TaskImperialExaminationActivityService {

	/**
	 * 根据code查询活动
	 * 
	 * @param code
	 *            活动代码
	 * @return {@link ImperialExaminationActivity}
	 */
	ImperialExaminationActivity get(long code);

	/**
	 * 查询最新的10个进行中的有效活动(目前我们认为系统只能同时处理10个进行中的活动)
	 * 
	 * @return {@link List} {@link ImperialExaminationActivity}
	 */
	List<ImperialExaminationActivity> listAllProcessingActivity();
}
