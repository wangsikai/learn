package com.lanking.uxb.service.imperialExamination.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;

public interface TaskImperialExaminationActivityAwardService {

	/**
	 * 存储
	 * 
	 * @param award
	 */
	void save(ImperialExaminationActivityAward award);

	/**
	 * 存储
	 * 
	 * @param award
	 */
	void save(Collection<ImperialExaminationActivityAward> awards);

	/**
	 * 最终排名出炉
	 * 
	 * @param activityCode
	 */
	List<ImperialExaminationActivityAward> queryRank(long activityCode,Integer room);

}
