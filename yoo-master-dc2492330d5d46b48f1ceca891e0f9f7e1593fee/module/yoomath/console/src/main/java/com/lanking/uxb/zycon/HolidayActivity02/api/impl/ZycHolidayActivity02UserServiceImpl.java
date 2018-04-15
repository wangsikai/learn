package com.lanking.uxb.zycon.HolidayActivity02.api.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02UserService;
import com.lanking.uxb.zycon.HolidayActivity02.form.ZycHolidayActivity02UserForm;

import httl.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity02UserServiceImpl implements ZycHolidayActivity02UserService {

	@Autowired
	@Qualifier("HolidayActivity02UserRepo")
	private Repo<HolidayActivity02User, Long> repo;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryUserByForm(ZycHolidayActivity02UserForm form, Pageable p) {
		Params param = Params.param();
		param.put("activityCode", form.getActivityCode());
		if (StringUtils.isNotBlank(form.getCreateAt())) {
			String start = form.getCreateAt() + "T00:00:00";
			String end = form.getCreateAt() + "T23:59:59";
			param.put("startAt", Date.from(LocalDateTime.parse(start)
					.atZone(ZoneId.systemDefault()).toInstant()));
			param.put("endAt", Date.from(LocalDateTime.parse(end)
					.atZone(ZoneId.systemDefault()).toInstant()));
		}
		if (StringUtils.isNotBlank(form.getAccountName())) {
			param.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getMobile())) {
			param.put("mobile", form.getMobile());
		}
		if (StringUtils.isNotBlank(form.getRealName())) {
			param.put("realName", '%' + form.getRealName() + '%');
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			param.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (StringUtils.isNotBlank(form.getChannelName())) {
			param.put("channelName", '%' + form.getChannelName() + '%');
		}
		
		return repo.find("$queryHoliday02User", param).fetch(p, Map.class);
	}

	@Override
	public HolidayActivity02User getUserActivityInfo(Long activityCode, Long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		return repo.find("$findByUserId", params).get();
	}

}
