package com.lanking.uxb.zycon.HolidayActivity02.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRank;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 总战力排名接口
 * 
 * @author peng.zhao
 * @version 2018-1-18
 */
public interface ZycHolidayActivity02PowerRankService {

	/**
	 * 根据用户查询rank
	 * 
	 * @param activityCode
	 * @param userId
	 */
	HolidayActivity02PowerRank getByUser(Long activityCode, Long userId);
	
	/**
	 * 更新总战力表
	 * 
	 * @param id 主键
	 * @param power 更新后的power值
	 */
	void updateByPower(Long id, Integer power, Long userId);
	
	/**
	 * 根据条件查询总战力
	 * 
	 * @param activityCode 活动code
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryPowerRankList(Long activityCode, Pageable p);
}
