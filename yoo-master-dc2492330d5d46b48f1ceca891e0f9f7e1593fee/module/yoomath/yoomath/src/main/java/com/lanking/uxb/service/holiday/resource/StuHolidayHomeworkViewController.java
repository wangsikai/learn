package com.lanking.uxb.service.holiday.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkItemConvert;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomework;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 学生假日作业查看相关rest API
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月23日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/holiday/view")
public class StuHolidayHomeworkViewController {

	@Autowired
	private HolidayStuHomeworkItemService holidayStuHomeworkItemService;
	@Autowired
	private HolidayStuHomeworkItemConvert holidayStuHomeworkItemConvert;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayHomeworkConvert holidayHomeworkConvert;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;

	/**
	 * 学生查看寒假作业
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(Long holidayStuHomeworkId) {
		HolidayStuHomework holidayStuHomework = holidayStuHomeworkService.get(holidayStuHomeworkId);
		if (holidayStuHomework.getStatus() == StudentHomeworkStatus.ISSUED && !holidayStuHomework.isViewed()) {
			holidayStuHomeworkService.updateViewStatus(holidayStuHomeworkId);
		}
		Map<String, Object> data = new HashMap<String, Object>(3);
		List<HolidayStuHomeworkItem> itemList = holidayStuHomeworkItemService.queryStuHkItems(
				holidayStuHomework.getHolidayHomeworkId(), Security.getUserId(), null, null);

		// 专项训练列表
		data.put("itemList", holidayStuHomeworkItemConvert.to(itemList));
		// 班级的信息
		VHolidayStuHomework vholidayStuHomework = holidayStuHomeworkConvert.to(holidayStuHomework);
		data.put("holidayStuHomework", vholidayStuHomework);
		data.put("holidayHomework", vholidayStuHomework.getHolidayHomework());
		// 我的进度查寒假作业列表的时候已经带过去
		return new Value(data);
	}
}
