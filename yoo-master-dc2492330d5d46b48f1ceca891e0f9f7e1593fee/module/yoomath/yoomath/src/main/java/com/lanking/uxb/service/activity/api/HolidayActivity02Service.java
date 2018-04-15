package com.lanking.uxb.service.activity.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02Service {

	/**
	 * 获取假期活动02配置
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	Map<String, Object> getActivityCfg(long code);

	/**
	 * 获取假期活动对象
	 * 
	 * @param code
	 * @return
	 */
	HolidayActivity02 get(long code);

	
}
