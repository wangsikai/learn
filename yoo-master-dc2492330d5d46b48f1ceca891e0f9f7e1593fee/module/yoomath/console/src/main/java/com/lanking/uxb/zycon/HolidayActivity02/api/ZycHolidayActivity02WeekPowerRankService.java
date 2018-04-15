package com.lanking.uxb.zycon.HolidayActivity02.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 周战力接口
 * 
 * @author peng.zhao
 * @version 2018-1-18
 */
public interface ZycHolidayActivity02WeekPowerRankService {

	/**
	 * 根据条件查询周战力
	 * 
	 * @param phase 周阶段
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryWeekPowerRankByPhase(List<Date> phase, Long activityCode, Pageable p);
	
	/**
	 * 更新用户周战力
	 * 
	 * @param activityCode 活动code
	 * @param id weekPower表主键
	 * @param power 调整后的power
	 */
	void updateWeekPower(Long activityCode, Long id, Integer power, Long userId);
}
