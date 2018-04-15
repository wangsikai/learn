package com.lanking.cloud.job.paperReport.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;

public interface StudentPaperReportDAO {

	/**
	 * 获取章节掌握情况(班级平均),只查大章节
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param textbookCode
	 * @return
	 */
	List<Map> getSectionMapByClassId(Long classId, Date startDate, Date endDate, Integer textbookCode);

	/**
	 * 获取章节掌握情况(学生自己),只查大章节<br>
	 * 新增知识点属性2017.11.25<br>
	 * 例如:【第一章 一元一次不等式】
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param textbookCode
	 * @return
	 */
	List<Map> getSectionMapByStuId(Long classId, Date startDate, Date endDate, Integer textbookCode);

	/**
	 * 获取小章节掌握情况(学生自己),只查小章节<br>
	 * 例如:【1.1 集合的含义及其表示】、【1.2 子集、全集、补集】、【1.3 交集、并集】
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param textbookCode
	 * @return
	 */
	List<Map> getSmallSectionMapByStuId(Long classId, Date startDate, Date endDate, Integer textbookCode);

	/**
	 * 获取学生正确率和完成率
	 * 
	 * @param studentId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map> getStuStat(Long classId, Date startDate, Date endDate);

	/**
	 * 获取班级正确率和完成率
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map<String, Object> getClazzStat(Long classId, Date startDate, Date endDate);

	StudentPaperReport save(StudentPaperReport report);

	List<StudentPaperReport> findByRecord(long recordId);

	/**
	 * 删除某个班级在时间段内的报告
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 */
	void deleteReport(Long classId, Date startDate, Date endDate);

	/**
	 * 获取班级作业情况，平均正确率，平均完成率
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map findClazzHkMap(Long classId, Date startDate, Date endDate);

	/**
	 * 获取班级作业情况，作业题目布置数，作业布置次数
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map findClazzHkMap2(Long classId, Date startDate, Date endDate);

	/**
	 * 获取学生作业情况
	 * 
	 * @param classId
	 * @param studentId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map findStuHkMap(Long classId, Long studentId, Date startDate, Date endDate);

	/**
	 * 获取学生作业情况
	 * 
	 * @param classId
	 * @param studentId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map findStuHkMap2(Long classId, Long studentId, Date startDate, Date endDate);

	/**
	 * 
	 * @param classId
	 * @param studentId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map> findLastHkList(Long classId, Long studentId, Date startDate, Date endDate);

	/**
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map> getKpMapByClassId(Long classId, Date startDate, Date endDate, Integer textbookCode);

	Long clazzSelfCount(Long classId, Date startDate, Date endDate);

	Long stuSelfCount(Long studentId, Date startDate, Date endDate);
}
