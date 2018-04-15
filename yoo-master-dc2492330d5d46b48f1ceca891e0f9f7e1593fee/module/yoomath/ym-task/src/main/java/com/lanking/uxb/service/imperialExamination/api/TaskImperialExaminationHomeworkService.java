package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

public interface TaskImperialExaminationHomeworkService {

	/**
	 * 创建
	 * 
	 * @param form
	 */
	void save(ImperialExaminationHomework ImperialExaminationHomework);

	/**
	 * 活动作业查询
	 * 
	 * @param activityCode
	 * @param type
	 */
	List<ImperialExaminationHomework> query(long activityCode, ImperialExaminationType type, Integer room, Integer category, Integer tag);

	/**
	 * 活动作业查询
	 * 
	 * @param activityCode
	 */
	List<ImperialExaminationHomework> query(long activityCode);

}
