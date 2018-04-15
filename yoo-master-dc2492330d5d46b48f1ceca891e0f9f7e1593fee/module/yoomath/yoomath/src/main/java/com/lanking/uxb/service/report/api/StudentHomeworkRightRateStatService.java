package com.lanking.uxb.service.report.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkRightRateStat;

public interface StudentHomeworkRightRateStatService {

	/**
	 * 获取正确率集合
	 * 
	 * @param days
	 * @param userId
	 * @return
	 */
	List<StudentHomeworkRightRateStat> findList(Integer days, Long userId);

	void inintData();
}
