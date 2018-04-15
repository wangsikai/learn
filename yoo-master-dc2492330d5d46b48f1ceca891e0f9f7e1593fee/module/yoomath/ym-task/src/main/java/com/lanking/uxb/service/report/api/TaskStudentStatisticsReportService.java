package com.lanking.uxb.service.report.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.StudentStatisticsReport;

/**
 * 学生统计
 * 
 * @since 2.8
 * @author zemin.song
 * @version 2016年11月4日 09:58:53
 */
public interface TaskStudentStatisticsReportService {

	/**
	 * 统计当前班级学生时间范围内作业相关数据
	 * 
	 * @param year
	 *            统计的年份
	 * @param month
	 *            统计的月份
	 * @param clazzId
	 *            学生所在班级
	 * @return
	 */
	void create(int year, int month, long clazzId);

	/**
	 * 创建单个学生的统计
	 * 
	 * @param userId
	 *            学生ID
	 * @param clazzId
	 *            班级ID
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param stuNum
	 *            班级总人数
	 * @param date
	 *            创建时间
	 * @param rank
	 *            排名
	 * @param textbookcode
	 *            教材
	 * @param subjectCode
	 *            学科code
	 * @return
	 */
	StudentStatisticsReport createStudentStat(long userId, long clazzId, int year, int month, int stuNum, Date date,
			Integer rank, long textbookCode, int subjectCode);

	/**
	 * 对应月份的统计记录
	 * 
	 * @param stuIds
	 *            学生ID 集合
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param subjectCode
	 *            学科
	 * @return
	 */
	Map<String, Long> getReportStuMap(List<Long> stuIds, int year, int month, Integer subjectCode);

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

}
