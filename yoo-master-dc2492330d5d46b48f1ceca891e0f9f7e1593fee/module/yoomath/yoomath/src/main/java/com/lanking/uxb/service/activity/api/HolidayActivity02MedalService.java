package com.lanking.uxb.service.activity.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Medal;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02MedalService {

	/*
	 * 查询出未已达到但未领取的勋章
	 */
	List<HolidayActivity02Medal> getNotReceivedMedals(Long activityCode,Long userId);
	
	/*
	 * 查询出该用户的所有勋章
	 */
	List<HolidayActivity02Medal> getMedals(Long activityCode,Long userId);
	
	/**
	 * 查询勋章
	 * 
	 * @id
	 */
	HolidayActivity02Medal get(Long id);
	
	/**
	 * 领取勋章
	 * 
	 * @param id 主键
	 * @param code 活动code
	 * @param difference 获得的成长值
	 * @param userId 用户ID
	 * @return HolidayActivity02Medal null:业务校验不通过,正常返回更新后的勋章
	 */
	HolidayActivity02Medal receiveMedal(Long id, Long code, Integer difference, Long userId); 
}
