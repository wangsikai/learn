package com.lanking.uxb.service.report.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.ClassStatisticsReport;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.report.value.VStudySection;

/**
 * 班级统计
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月5日 下午6:46:04
 */
public interface ClassStatisticsReportService {

	/**
	 * 获得一个指定班级，指定日期的报告.
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
	ClassStatisticsReport getClassReport(int subjectCode, long classId, int year, int month);

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
	 * 获得指定最小年月的班级集合（有数据）
	 * 
	 * @param classIds
	 *            当前班级
	 * @param minDate
	 *            最小年月
	 * @param maxDate
	 *            最大年月
	 * @return
	 */
	List<HomeworkClazz> getClazzByMinDate(List<Long> classIds, Date minDate, Date maxDate);

	/**
	 * 获得指定最小年月的年月集合
	 * 
	 * @param subjectCode
	 *            科目代码
	 * @param teacherId
	 *            教师ID
	 * @param clazzId
	 *            班级ID
	 * @param minDate
	 *            最小年月
	 * @param maxDate
	 *            最大年月
	 * @return
	 */
	List<Map> getDatesByMinDate(Long clazzId, Date minDate, Date maxDate);

	boolean existReport(int year, int month, long classId);

	/**
	 * 获取最小章节对应做题数、做对题目数
	 * 
	 * @param classId
	 * @return
	 */
	List<Map> findSectionMasterList(Long classId);

	/**
	 * 组装章节掌握情况数据
	 * 
	 * @param classId
	 * @param vsections
	 * @return
	 */
	List<VStudySection> handle(Long classId, List<VSection> vsections);

	/**
	 * 获取班级最新的学情报告的年月，没有返回null
	 * 
	 * @param classId
	 * @return
	 */
	String getLastest(Long classId);

	/**
	 * 显示作业涉及到的最新的一章,取最大的一个章节
	 * 
	 * @param classId
	 * @param textbookCode
	 * @return
	 */
	Long getMaxSection(Long classId, Integer textbookCode);

	/**
	 * 获取指定第一层章节的做题情况
	 * 
	 * @param classId
	 * @param sectionCodes
	 * @return
	 */
	Map<Long, Integer> getSectionDoCountMap(Long classId, List<Long> sectionCodes);

	/**
	 * 获取章节下薄弱知识点
	 * 
	 * @param classId
	 * @param sectionCode
	 * @return
	 */
	List<Long> getWeakKpListBySectionCode(long classId, long sectionCode);
}
