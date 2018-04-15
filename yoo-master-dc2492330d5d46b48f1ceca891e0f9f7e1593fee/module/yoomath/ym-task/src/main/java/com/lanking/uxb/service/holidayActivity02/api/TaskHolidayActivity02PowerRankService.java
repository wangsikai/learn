package com.lanking.uxb.service.holidayActivity02.api;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface TaskHolidayActivity02PowerRankService {
	/**
	 * 统计排名
	 * 
	 * @param activity
	 *            活动
	 */
	void processRank(HolidayActivity02 activity);
	
}
