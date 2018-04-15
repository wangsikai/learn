package com.lanking.stuWeekReport.dao;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.StudentHomework;

public interface StudentHomeworkDAO {

	/**
	 * 查询学生作业情况
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<StudentHomework> findByUserId(Long userId, String startDate, String endDate);

	/**
	 * 获取章节掌握情况
	 * 
	 * @param studentId
	 * @param textbookCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getSectionAnalysis(Long studentId, Integer textbookCode, String startDate, String endDate);

	Map<String, Object> getStatByUser(Long userId, String startDate, String endDate);

	/**
	 * 查询班级下学生周数据
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map> stuClassWeekReportList(Long classId, String startDate, String endDate);
}
