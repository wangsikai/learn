package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

public interface TaskImperialExaminationActivityRankStatService {

	/**
	 * 统计排名
	 */
	void countRank(long activityCode, ImperialExaminationType type, Integer lowerTimes,Integer higherTimes);

	/**
	 * 综合排名统计
	 */
	void statActivityAward(long activityCode);

	/**
	 * 最终老师奖品排名
	 */
	void setAwardRank(List<ImperialExaminationActivityAward> awards,Integer room);
	
	/**
	 * 最终学生奖品排名
	 */
	void setAwardRankStudent(List<ImperialExaminationActivityAwardStudent> awards);

}
