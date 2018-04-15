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
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02PKRecordService;
import com.lanking.uxb.zycon.HolidayActivity02.form.ZycHolidayActivity02PKRecordForm;

import httl.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity02PKRecordServiceImpl implements ZycHolidayActivity02PKRecordService {

	@Autowired
	@Qualifier("HolidayActivity02PKRecordRepo")
	private Repo<HolidayActivity02PKRecord, Long> repo;
	
	@Override
	public Page<HolidayActivity02PKRecord> getRedoresByForm(ZycHolidayActivity02PKRecordForm form, Pageable p) {
		Params params = Params.param();
		params.put("activityCode", form.getActivityCode());
		if (StringUtils.isNotBlank(form.getPkAt())) {
			String start = form.getPkAt() + "T00:00:00";
			String end = form.getPkAt() + "T23:59:59";
			params.put("startAt", Date.from(LocalDateTime.parse(start)
					.atZone(ZoneId.systemDefault()).toInstant()));
			params.put("endAt", Date.from(LocalDateTime.parse(end)
					.atZone(ZoneId.systemDefault()).toInstant()));
		}
		if (StringUtils.isNotBlank(form.getAccountName())) {
			params.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getRealName())) {
			params.put("realName", '%' + form.getRealName() + '%');
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			params.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (form.getRealMan() != null) {
			params.put("realMan", form.getRealMan());
		}
		
		return repo.find("$queryRedoresByForm", params).fetch(p);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map queryUserInfoById(Long pkId) {
		Params params = Params.param();
		params.put("pkId", pkId);
		return repo.find("$queryUserInfoById", params).get(Map.class);
	}

}
