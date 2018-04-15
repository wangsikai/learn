package com.lanking.stuWeekReport.service;

import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface StuWeekReportStatService {

	/**
	 * 班级统计完再统计所有的,有先后顺序
	 * 
	 * @param classId
	 */
	void statStuClassWeekReport(Long classId, String createTime);

	void stat(Long userId, String createTime);

	CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable);

	CursorPage<Long, Map> queryClassIds(CursorPageable<Long> pageable);

	/**
	 * 更新student_week_report---right_rate_class_ranks
	 * 
	 * @param userId
	 */
	void statRightRateClassRanks(Long userId, String startDate, String endDate);

}
