package com.lanking.uxb.zycon.activity.api;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.uxb.zycon.activity.form.ZycHolidayActivity02ConfigForm;

public interface ZycHolidayActivity02ConfigService {

	/**
	 * 保存和编辑
	 * 
	 * @param form
	 */
	void save(ZycHolidayActivity02ConfigForm form);

	/**
	 * 获取最大code
	 * 
	 * @return
	 */
	long maxCode();

	/**
	 * 
	 */
	HolidayActivity02 queryByCode(Long code);

}
