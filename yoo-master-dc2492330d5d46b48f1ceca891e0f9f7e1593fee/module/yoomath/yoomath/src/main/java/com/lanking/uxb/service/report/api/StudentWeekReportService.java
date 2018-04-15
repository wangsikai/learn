package com.lanking.uxb.service.report.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;

public interface StudentWeekReportService {

	/**
	 * 获取用户的周统计报表(所有班级的数据总和)
	 * 
	 * @param userId
	 * @return
	 */
	List<StudentWeekReport> getByUserId(Long userId);

	/**
	 * 通过用户id和指定周时间范围查询唯一的周统计数据
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	StudentWeekReport findWeekReport(Long userId, String startDate, String endDate);
}
