package com.lanking.uxb.service.diagnostic.api.student;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 学生诊断 - 班级最近n次作业统计服务.
 * 
 * @author wangsenhao
 * 
 * @since 学生端，2017-6-30 数据跟随相关整理
 */
public interface StaticStuDiagnosticClassLatestHomeworkService {

	void deleteLastestByStuClass(long classId, Long studentId);

	/**
	 * 统计学生班级最近n次作业(只针对普通作业)<br>
	 * 对应表：diagno_stu_class_latest_hk
	 * 
	 * @param studentId
	 *            学生id
	 * @param homeworkId
	 *            作业id
	 * @param rank
	 *            排名
	 * @param rightRate
	 *            正确率
	 */
	void statStuClassLatestHk(long studentId, long homeworkId, Integer rank, BigDecimal rightRate);

	/**
	 * 统计学生班级最近n次作业知识点掌握情况(只针对普通作业)<br>
	 * 对应表：diagno_stu_class_latest_hk_kp
	 * 
	 * @param stuHkIds
	 *            学生studentHomeworkId集合
	 * @param studentId
	 *            学生ID
	 * @param classId
	 *            班级ID
	 */
	void statStuClassLatestHkKp(List<Long> stuHkIds, long studentId, long classId);

	/**
	 * 统计学生班级可以看到的教材(所做题目涉及到的教材)<br>
	 * 对应表：diagno_class_textbook
	 * 
	 * @param inTextbookCodes
	 *            包含的教材集合
	 * @param studentId
	 * @param classId
	 */
	void statStuClassTextbook(Set<Integer> inTextbookCodes, long studentId, long classId);

	/**
	 * 统计学生班级最近30次作业，教材维度下的做题情况<br>
	 * 业务变更:1.学生移出和加入班级，重新计算最近三十次<br>
	 * 对应表：diagno_stu_class
	 * 
	 * @param stuHkIds
	 *            学生studentHomeworkId集合
	 * @param studentId
	 * @param classId
	 */
	void statStuClassLatestByTextbook(List<Long> stuHkIds, long studentId, long classId);

	/**
	 * 最近30次作业统计入口,每次作业下发调用<br>
	 * 
	 * @param classId
	 * @param homeworkId
	 */
	void statStuClassLastestData(long classId, Long homeworkId);

	void statOneStuClassLastestData(long studentId, long classId);

	/**
	 * 最近30次作业统计入口
	 * 
	 * @param studentId
	 * @param classId
	 */
	void statStuClassLastestDataByStuId(long studentId, Long classId);

	/**
	 * 班级学生移出或加入，最近30次数据重新统计<br>
	 * 场景描述：<br>
	 * 1.学生加入班级;<br>
	 * 2.老师同意加入班级;<br>
	 * 3.管控台班级管理,移除和添加学生;<br>
	 * 以上几种情况都重新统计，因为学生有可能以前是这个班的做过作业，后来下发过作业，这部分数据就删除了
	 * 
	 * @param formClassId
	 * @param toClassId
	 */
	void statWhenJoin(long studentId, Long classId);

	void statWhenLeave(long studentId, Long classId);

	/**
	 * 处理学生所有班级数据的，对应表student_homework_statistic<br>
	 * 临时存放，后面可能会移动到其他地方
	 * 
	 * @param homeworkId
	 */
	void stuHkStatistic(Long homeworkId);

	/**
	 * 初始化所有学生数据，对应表student_homework_statistic<br>
	 */
	void initStuHkStatistic(List<Long> studentIds);

	List<Map> stuHkStat(Long classId);

	List<Map> stuHkStatByStudentIds(List<Long> studentIds);

	Map<Long, Integer> rateMapByStudentIds(List<Long> studentIds);

	Map<Long, Integer> rateMap(Long classId);

}
