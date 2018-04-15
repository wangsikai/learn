package com.lanking.uxb.zycon.homework.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 作业Service
 *
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
public interface ZycHomeworkService {
	/**
	 * 获得作业
	 *
	 * @param id
	 *            id
	 * @return {@link Homework}
	 */
	Homework get(long id);

	/**
	 * mget
	 *
	 * @param ids
	 *            ids集合
	 * @return {@link Homework}
	 */
	Map<Long, Homework> mget(Collection<Long> ids);

	/**
	 * 移除列表
	 *
	 * @param homeworkId
	 *            作业id
	 */
	void remove(long homeworkId);

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
