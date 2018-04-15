package com.lanking.uxb.zycon.activity.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01Service;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01StatisticsService;
import com.lanking.uxb.zycon.activity.form.ZycHolidayActivity01Form;
import com.lanking.uxb.zycon.activity.value.VPageHolidayActivity01Rank;

import httl.util.StringUtils;

/**
 * 暑期作业活动相关.
 * 
 * @since 教师端 v1.2.0
 *
 */
@RestController
@RequestMapping(value = "zyc/holidayActivity01Rank")
public class ZycHolidayActivity01RankController {
	@Autowired
	private ZycHolidayActivity01StatisticsService holidayActivity01StatisticsService;

	@Autowired
	private ZycHolidayActivity01Service holidayActivity01Service;

	/**
	 * 排名统计
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RequestMapping(value = "queryRank", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryUser(ZycHolidayActivity01Form form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		if (form.getActivityCode() == null) {
			return new Value(new MissingArgumentException());
		}
		HolidayActivity01 holidayActivity01 = holidayActivity01Service.get(form.getActivityCode());
		if (StringUtils.isBlank(form.getEndPeriodTime())) {
			form.setAll(true);
			List<List<Long>> periods = holidayActivity01.getCfg().getPeriods();
			Date startDate = new Date(periods.get(0).get(0));
			Date endDate = new Date(periods.get(periods.size() - 1).get(1));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			form.setStartPeriodTime(df.format(startDate));
			form.setEndPeriodTime(df.format(endDate));
		}
		Page<Map> cp = holidayActivity01StatisticsService.queryActivityRank(form, P.index(page, pageSize));
		VPageHolidayActivity01Rank<Map> vp = new VPageHolidayActivity01Rank<Map>();
		if (page == 1) {
			vp.setPeriods(holidayActivity01.getCfg().getPeriods());
		}
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(cp.getItems());
		return new Value(vp);
	}

}
