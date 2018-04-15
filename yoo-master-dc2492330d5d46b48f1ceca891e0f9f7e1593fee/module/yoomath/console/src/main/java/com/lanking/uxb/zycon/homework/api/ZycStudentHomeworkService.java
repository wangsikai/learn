package com.lanking.uxb.zycon.homework.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.StudentHomework;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public interface ZycStudentHomeworkService {

	StudentHomework get(Long id);

	/**
	 * 根据Homework.id查找所有学生作业
	 *
	 * @param hkId
	 *            Homework Id
	 * @return {@link StudentHomework}
	 */
	List<StudentHomework> listByHomework(long hkId);

	/**
	 * 班级学生时间范围内 作业排名情况
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param clazzId
	 *            班级
	 * @param stuIds
	 *            学生
	 * @return key为学生ID，value 为排名
	 */
	Map<Long, Integer> getStuStatRankByClass(int year, int month, long clazzId, List<Long> stuIds);

	/**
	 * 获取对应年月的学生作业（在该月布置和下发的作业）
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param userId
	 *            用户id
	 * @param clazzId
	 *            班级ID
	 * @return
	 */
	List<StudentHomework> listByTime(int year, int month, long userId, long clazzId);
}
