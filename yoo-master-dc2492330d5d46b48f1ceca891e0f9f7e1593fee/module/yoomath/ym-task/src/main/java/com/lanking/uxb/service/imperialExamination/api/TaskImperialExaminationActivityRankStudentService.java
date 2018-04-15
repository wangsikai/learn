package com.lanking.uxb.service.imperialExamination.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRankStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

public interface TaskImperialExaminationActivityRankStudentService {

	void save(ImperialExaminationActivityRankStudent rank);
	
	/**
	 * 存储
	 * 
	 * @param ranks
	 */
	void save(Collection<ImperialExaminationActivityRankStudent> ranks);
	
	@SuppressWarnings("rawtypes")
	List<Map> queryStudentScoreByClazzId(long activityCode,ImperialExaminationType type);
	
	List<ImperialExaminationActivityRankStudent> queryAllStudentRanks(long activityCode, ImperialExaminationType type,
			                                               Integer tag);

}
