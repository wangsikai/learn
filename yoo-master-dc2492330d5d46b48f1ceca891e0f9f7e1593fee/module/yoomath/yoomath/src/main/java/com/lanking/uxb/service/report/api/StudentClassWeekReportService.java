package com.lanking.uxb.service.report.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.StudentClassWeekReport;

public interface StudentClassWeekReportService {

	/**
	 * 获取班级下的学生正确率情况
	 * 
	 * @param classIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<StudentClassWeekReport> findList(List<Long> classIds, String startDate, String endDate);

	/**
	 * 查询指定用户指定周的数据不同班级下的数据
	 * 
	 * @param userId
	 * @param classIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map<Long, StudentClassWeekReport> getReportMap(Long userId, List<Long> classIds, String startDate, String endDate);

}
