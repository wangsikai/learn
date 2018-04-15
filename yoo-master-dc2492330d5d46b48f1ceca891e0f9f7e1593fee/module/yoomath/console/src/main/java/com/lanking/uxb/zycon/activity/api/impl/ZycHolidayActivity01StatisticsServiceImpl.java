/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Statistics;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01StatisticsService;
import com.lanking.uxb.zycon.activity.form.ZycHolidayActivity01Form;

import httl.util.StringUtils;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity01StatisticsServiceImpl implements ZycHolidayActivity01StatisticsService {

	@Autowired
	@Qualifier("HolidayActivity01StatisticsRepo")
	private Repo<HolidayActivity01Statistics, Long> HolidayActivity01StatisticsRepo;

	@Override
	public Page<Map> queryActivityRank(ZycHolidayActivity01Form form, Pageable p) {
		Params params = Params.param("activityCode", form.getActivityCode());
		params.put("isAll", form.isAll());
		if (StringUtils.isNotBlank(form.getAccountName())) {
			params.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getChannelName())) {
			params.put("channelName", '%' + form.getChannelName() + '%');
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			params.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (StringUtils.isNotBlank(form.getClazzName())) {
			params.put("clazzName", '%' + form.getClazzName() + '%');
		}
		if (StringUtils.isNotBlank(form.getRealName())) {
			params.put("realName", '%' + form.getRealName() + '%');
		}
		if (form.getPhase() != null) {
			params.put("phase", form.getPhase());
		}
		if (StringUtils.isNotBlank(form.getStartPeriodTime())) {
			params.put("startPeriodTime", form.getStartPeriodTime());
		}
		if (StringUtils.isNotBlank(form.getEndPeriodTime())) {
			params.put("endPeriodTime", form.getEndPeriodTime());
		}
		return HolidayActivity01StatisticsRepo.find("$queryActivityStatisticsRank", params).fetch(p, Map.class);
	}

}
