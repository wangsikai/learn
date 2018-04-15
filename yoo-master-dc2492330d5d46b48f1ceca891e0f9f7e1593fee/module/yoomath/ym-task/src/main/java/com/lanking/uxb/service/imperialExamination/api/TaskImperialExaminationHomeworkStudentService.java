package com.lanking.uxb.service.imperialExamination.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

public interface TaskImperialExaminationHomeworkStudentService {

	/**
	 * 创建学生活动作业
	 * 
	 * @param form
	 */
	void save(ImperialExaminationHomeworkStudent imperialExaminationHomeworkStudent);
	/**
	 * 存储
	 * 
	 * @param award
	 */
	void save(Collection<ImperialExaminationHomeworkStudent> homeworks);

	/**
	 * 学生活动作业查询
	 * 
	 * @param activityCode
	 * @param type
	 */
	List<ImperialExaminationHomeworkStudent> query(long activityCode, ImperialExaminationType type, Integer room, Integer category, Integer tag);

	/**
	 * 学生活动作业查询
	 * 
	 * @param activityCode
	 */
	List<ImperialExaminationHomeworkStudent> query(long activityCode);

}
