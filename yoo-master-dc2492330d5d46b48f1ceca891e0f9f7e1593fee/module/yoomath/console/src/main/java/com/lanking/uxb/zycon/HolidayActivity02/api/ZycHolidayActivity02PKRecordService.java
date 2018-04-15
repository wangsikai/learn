package com.lanking.uxb.zycon.HolidayActivity02.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.HolidayActivity02.form.ZycHolidayActivity02PKRecordForm;

/**
 * 活动对战记录接口
 * 
 * @author peng.zhao
 * @version 2018-1-17
 */
public interface ZycHolidayActivity02PKRecordService {

	/**
	 * 查询对战记录
	 * 
	 * @param form 查询条件
	 */
	Page<HolidayActivity02PKRecord> getRedoresByForm(ZycHolidayActivity02PKRecordForm form, Pageable p);
	
	/**
	 * 根据pkId查询选手帐号、真实姓名、学校、正确率
	 */
	@SuppressWarnings("rawtypes")
	Map queryUserInfoById(Long pkId);
}
