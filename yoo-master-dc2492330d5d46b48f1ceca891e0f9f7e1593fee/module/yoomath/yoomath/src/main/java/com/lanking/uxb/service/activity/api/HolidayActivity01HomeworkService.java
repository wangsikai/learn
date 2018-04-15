package com.lanking.uxb.service.activity.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Homework;

/**
 * 假期活动01-参与活动的用户布置的作业service.
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年6月14日
 */
public interface HolidayActivity01HomeworkService {

	/**
	 * 获得教师用户名下的活动作业数据，按照百分比返回.
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<String, Object> getPercent(long code, long userId, HolidayActivity01Cfg cfg);

	/**
	 * 批量创建
	 * 
	 * @param homeworks
	 *            作业集合
	 * @param holidayActivity01
	 *            活动
	 * @param teacherId
	 *            教师
	 * 
	 * @return
	 */
	void create(Collection<HolidayActivity01Homework> homeworks, HolidayActivity01 holidayActivity01, long teacherId);

	/**
	 * 判断当前用户是否布置过假期活动作业
	 * 
	 * @param code
	 * @param userId
	 * @return
	 */
	Long countHk(long code, long userId);
}
