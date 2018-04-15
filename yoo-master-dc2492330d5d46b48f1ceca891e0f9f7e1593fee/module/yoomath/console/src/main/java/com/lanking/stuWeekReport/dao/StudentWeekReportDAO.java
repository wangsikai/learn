package com.lanking.stuWeekReport.dao;

import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;

public interface StudentWeekReportDAO {

	/**
	 * 通过用户查询对应的周统计数据
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	StudentWeekReport getByUser(Long userId, String startDate, String endDate);

	StudentWeekReport save(StudentWeekReport sw);

}
