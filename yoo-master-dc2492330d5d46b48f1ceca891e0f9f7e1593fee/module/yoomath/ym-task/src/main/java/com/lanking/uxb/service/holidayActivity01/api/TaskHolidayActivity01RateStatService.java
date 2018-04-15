package com.lanking.uxb.service.holidayActivity01.api;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;

/**
 * 假期活动统计入口
 * 
 * @author zemin.song
 *
 */
public interface TaskHolidayActivity01RateStatService {
	/**
	 * 开始统计
	 * 
	 * @return
	 */
	void statRate(HolidayActivity01 activity);

}
