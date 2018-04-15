package com.lanking.uxb.service.report.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentStatisticsReport;

/**
 * 学生统计
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月5日 下午6:46:04
 */
public interface StudentStatisticsReportService {

	/**
	 * 获得多个指定学生，指定日期的报告.
	 * 
	 * @param subjectCode
	 *            科目代码
	 * @param stuIds
	 *            学生IDS
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return
	 */
	List<StudentStatisticsReport> getStuStatReports(int subjectCode, List<Long> stuIds, int year, int month);

	/**
	 * 获得一个指定班级，指定日期的报告.
	 * 
	 * @param studentId
	 *            学生ID
	 * 
	 * @param classId
	 *            班级代码
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return
	 */
	StudentStatisticsReport getStudentReport(long studentId, long classId, int year, int month);

	/**
	 * 获得指定ID的报告.
	 * 
	 * @param id
	 * @return
	 */
	StudentStatisticsReport get(Long id);

	/**
	 * 购买报告.
	 * 
	 * @param reportId
	 *            报告ID
	 */
	void buReport(Long reportId);

	/**
	 * 获得指定最小年月的班级集合
	 * 
	 * @param subjectCode
	 *            科目代码
	 * @param studentId
	 *            学生ID
	 * @param minDate
	 *            最小年月
	 * @param maxDate
	 *            最大年月
	 * @param buy
	 *            购买标记
	 * @return
	 */
	List<HomeworkClazz> getClazzByMinDate(Integer subjectCode, long studentId, Date minDate, Date maxDate, Boolean buy);

	/**
	 * 获得指定最小年月的年月集合
	 * 
	 * @param subjectCode
	 *            科目代码
	 * @param studentId
	 *            学生ID
	 * @param clazzId
	 *            班级ID
	 * @param minDate
	 *            最小年月
	 * @param maxDate
	 *            最大年月
	 * @param buy
	 *            购买标记
	 * @return
	 */
	List<String> getDatesByMinDate(Integer subjectCode, long studentId, Long clazzId, Date minDate, Date maxDate,
			Boolean buy);

	boolean existReport(int year, int month, long studentId);
}
