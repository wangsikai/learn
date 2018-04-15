package com.lanking.cloud.job.stuWeekReport.service;

import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface StuWeekReportStatService {

	/**
	 * 班级统计完再统计所有的,有先后顺序
	 * 
	 * @param classId
	 */
	void statStuClassWeekReport(Long classId);

	void stat(Long userId);

	CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable, int modVal);

	CursorPage<Long, Map> queryClassIds(CursorPageable<Long> pageable, int modVal);

	/**
	 * 更新student_week_report---right_rate_class_ranks
	 * 
	 * @param userId
	 */
	void statRightRateClassRanks(Long userId, String startDate, String endDate);

}
