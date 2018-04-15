package com.lanking.uxb.service.report.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseSection;

/**
 * 班级学情报告用的相关业务接口集合
 * 
 * @since 2.6.0
 * @author wangsenhao
 *
 */
public interface TaskClassReportBaseService {
	/**
	 * 获取老师当前有效班级
	 * 
	 * @param teacherId
	 * @return
	 */
	List<HomeworkClazz> listCurrentClazzs(long teacherId);

	/**
	 * 获取用户 对应班级 ，年份，月份布置和下发的作业
	 * 
	 * @param userId
	 *            用户ID
	 * @param clazzId
	 *            班级ID
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return
	 */
	List<Homework> listByTime(long userId, long clazzId, int year, int month);

	/**
	 * 获取时间月份内 某个班级 第一份下发的作业
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param id
	 *            班级ID
	 * @return homework
	 */
	Homework getFirstIssuedHomeworkInMonth(int year, int month, long classId);

	/**
	 * 根据id获得数据
	 *
	 * @param id
	 *            id
	 * @return {@link StudentExerciseSection}
	 */
	StudentExerciseSection get(long id);

	/**
	 * 根据Section获得数据
	 *
	 * @param userId
	 *            学生id
	 * @param sectionCode
	 *            章节码
	 * @return {@link StudentExerciseSection}
	 */
	StudentExerciseSection getBySection(long userId, long sectionCode);

	/**
	 * mget　by 章节码
	 *
	 * @param userId
	 *            学生id
	 * @param sectionCodes
	 *            章节码
	 * @param lastMonth
	 *            对应的上个月如 201602
	 * @return {@link StudentExerciseSection}
	 */
	Map<Long, StudentExerciseSection> mgetBySection(long userId, Collection<Long> sectionCodes, Long lastMonth);

	/**
	 * 查找一个班级学生平均掌握情况
	 * 从1.9开始，寒假作业功能中需要查看班级平均掌握情况，此时传入的sectionCode为教材下的<strong>一级目录</strong>
	 *
	 * @since yoomath V1.9
	 * @param classId
	 *            班级id
	 * @param sectionCode
	 *            章节码
	 * @param lastMonth
	 *            月份 如 201602
	 * @return {@link StudentExerciseSection}
	 */
	List<StudentExerciseSection> findByClassIdAndSectionCode(long classId, long sectionCode, Long lastMonth);

	/**
	 * 获取学生平均正确率(上个月)
	 * 
	 * @param classId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Map> findStuAvgRightRateList(long classId, Date startTime, Date endTime);
}
