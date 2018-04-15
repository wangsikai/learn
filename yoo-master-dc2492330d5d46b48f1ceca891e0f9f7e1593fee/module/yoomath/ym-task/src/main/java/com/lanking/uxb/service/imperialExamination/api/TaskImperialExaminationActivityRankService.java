package com.lanking.uxb.service.imperialExamination.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

public interface TaskImperialExaminationActivityRankService {

	void save(ImperialExaminationActivityRank rank);
	
	/**
	 * 存储
	 * 
	 * @param ranks
	 */
	void save(Collection<ImperialExaminationActivityRank> ranks);
	
	@SuppressWarnings("rawtypes")
	List<Map> queryTeacherScoreByClazzId(long activityCode,Integer room,ImperialExaminationType type);

	List<ImperialExaminationActivityRank> queryAllTeacherRanks(long activityCode, ImperialExaminationType type,
			Integer tag,Integer room);

}
