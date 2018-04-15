package com.lanking.uxb.zycon.activity.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity02ConfigService;
import com.lanking.uxb.zycon.activity.form.ZycHolidayActivity02ConfigForm;

/**
 * 学生端假期活动02一些基本的配置<br>
 * 1.提供活动开始，结束时间及各阶段时间的编辑显示<br>
 * 2.提供配置的编辑<br>
 * 3.提供数据的生成
 * 
 * @author qiuxue.jiang
 * @version 2018年1月17日
 */
@RestController
@RequestMapping(value = "zyc/holidayactivity02/config")
public class ZycHolidayActivity02ConfigController {

	@Autowired
	private ZycHolidayActivity02ConfigService configService;

	/**
	 * 保存
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
	public Value save(ZycHolidayActivity02ConfigForm form) {
		configService.save(form);
		return new Value();
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "queryByCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryByCode(Long code) {
		HolidayActivity02 m = configService.queryByCode(code);
		return new Value(m);
	}

}
