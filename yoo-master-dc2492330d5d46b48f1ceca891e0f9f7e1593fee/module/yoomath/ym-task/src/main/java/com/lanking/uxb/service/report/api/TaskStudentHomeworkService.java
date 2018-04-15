package com.lanking.uxb.service.report.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.StudentHomework;

/**
 * 学生作业接口
 * 
 * @since 2.6.0
 * @author zemin.song
 * @version 2016年11月4日 10:40:37
 */
public interface TaskStudentHomeworkService {
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
	 * 1 获取对应年月的学生作业（在该月布置和下发的作业）
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
