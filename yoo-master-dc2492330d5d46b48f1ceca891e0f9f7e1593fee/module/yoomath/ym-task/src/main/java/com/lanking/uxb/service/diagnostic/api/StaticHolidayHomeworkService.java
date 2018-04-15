package com.lanking.uxb.service.diagnostic.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;

/**
 * 假期作业相关数据处理
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface StaticHolidayHomeworkService {
	/**
	 * 根据id得到假期作业对象
	 *
	 * @param id
	 *            假期作业id
	 * @return {@link HolidayHomework}
	 */
	HolidayHomework get(long id);

	/**
	 * 得到以下发的假期作业各专项题目情况
	 *
	 * @param id
	 *            假期作业id
	 * @return {@link List}
	 */
	List<Map> findHolidayHkItemQuestion(long id);

	/**
	 * 根据班级获得前一天下发的假期作业
	 *
	 * @param classId
	 *            班级id
	 * @return 假期作业列表
	 */
	List<HolidayHomework> getHks(long classId);

	/**
	 * 查询一个班级所有已经下发的假期作业
	 *
	 * @param classId
	 *            班级id
	 * @return 假期作业列表
	 */
	List<HolidayHomework> findAll(long classId);
}
