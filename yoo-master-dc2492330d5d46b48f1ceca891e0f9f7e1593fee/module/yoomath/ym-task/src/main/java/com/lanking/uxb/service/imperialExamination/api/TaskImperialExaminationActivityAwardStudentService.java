package com.lanking.uxb.service.imperialExamination.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;

public interface TaskImperialExaminationActivityAwardStudentService {

	/**
	 * 存储
	 * 
	 * @param award
	 */
	void save(ImperialExaminationActivityAwardStudent award);

	/**
	 * 存储
	 * 
	 * @param award
	 */
	void save(Collection<ImperialExaminationActivityAwardStudent> awards);

	/**
	 * 最终排名出炉
	 * 
	 * @param activityCode
	 */
	List<ImperialExaminationActivityAwardStudent> queryRank(long activityCode);

}
