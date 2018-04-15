package com.lanking.uxb.service.report.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 任务 作业相关接口
 * 
 * @author wangsenhao
 *
 */
public interface TaskHomeworkService {

	/**
	 * 获取作业
	 * 
	 * @param id
	 *            作业ID
	 * @return {@link Homework}
	 */
	Homework get(long id);

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
}
