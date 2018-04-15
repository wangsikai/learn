package com.lanking.uxb.service.report.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.stat.ClassStatisticsReport;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 班级统计相关接口
 * 
 * @author wangsenhao
 *
 */
public interface TaskClassStatisticsReportService {

	/**
	 * 统计一个老师所教的班级数据接口
	 * 
	 * @param year
	 *            统计的年份
	 * @param month
	 *            统计的月份
	 * @param teacher
	 *            teacher 对象
	 * @return
	 */
	void create(int year, int month, Teacher teacher);

	/**
	 * 统计一个班级的作业相关数据
	 * 
	 * @param userId
	 *            用户Id
	 * @param clazzId
	 *            班级Id
	 * @param year
	 *            统计的年份
	 * @param month
	 *            统计的月份
	 * @param stuNum
	 *            学生人数
	 * @param date
	 *            创建日期
	 * @param textbookcode
	 *            教材code
	 * @param subjectCode
	 *            科目code
	 * @return
	 */
	ClassStatisticsReport createClazzStat(long userId, long clazzId, int year, int month, int stuNum, Date date,
			Integer textbookCode, int subjectCode);

	/**
	 * 获取已经统计过的教师班级MAP
	 * 
	 * @param classIds
	 *            班级ID集合
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param subjectcode
	 *            科目code
	 * @return
	 */
	Map<String, Long> getReportClassMap(List<Long> classIds, int year, int month, int subjectCode);

	/**
	 * 获得多个指定班级，指定日期的报告.
	 * 
	 * @param subjectCode
	 *            科目代码
	 * @param classId
	 *            班级代码
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return
	 */
	List<ClassStatisticsReport> getClassReports(int subjectCode, List<Long> classIds, int year, int month);

	/**
	 * 获取所有老师
	 * 
	 * @param cursorPageable
	 * 
	 * @return
	 */
	CursorPage<Long, Teacher> getAll(CursorPageable<Long> cursorPageable);

	List<Map<String, Object>> getKnowpointAnalysis(Long clazzId, Integer textbookCode);

	List<Map<String, Object>> getSectionAnalysis(Long clazzId, Integer textbookCode);

}
