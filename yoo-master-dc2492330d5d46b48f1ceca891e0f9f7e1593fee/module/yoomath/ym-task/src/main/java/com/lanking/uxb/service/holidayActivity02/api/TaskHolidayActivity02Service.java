package com.lanking.uxb.service.holidayActivity02.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;

public interface TaskHolidayActivity02Service {

	/**
	 * 根据code查询活动
	 * 
	 * @param code
	 *            活动代码
	 * @return {@link HolidayActivity02}
	 */
	HolidayActivity02 get(long code);

	/**
	 * 查询最新的10个进行中的有效活动(目前我们认为系统只能同时处理10个进行中的活动)
	 * 
	 * @return {@link List} {@link HolidayActivity02}
	 */
	List<HolidayActivity02> listAllProcessingActivity();
}
