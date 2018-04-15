package com.lanking.uxb.zycon.HolidayActivity02.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.HolidayActivity02.form.ZycHolidayActivity02UserForm;

/**
 * 活动用户接口
 * 
 * @author peng.zhao
 * @version 2018-1-16
 */
public interface ZycHolidayActivity02UserService {

	/**
	 * 根据条件查询用户
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryUserByForm(ZycHolidayActivity02UserForm form, Pageable p);
	
	/**
	 * 查询该用户的参赛信息
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	HolidayActivity02User getUserActivityInfo(Long activityCode, Long userId);
}
