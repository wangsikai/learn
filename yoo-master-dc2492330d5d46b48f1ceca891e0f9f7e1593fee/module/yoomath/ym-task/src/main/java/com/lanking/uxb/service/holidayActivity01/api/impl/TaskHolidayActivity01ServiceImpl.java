package com.lanking.uxb.service.holidayActivity01.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01Service;

@Transactional(readOnly = true)
@Service
public class TaskHolidayActivity01ServiceImpl implements TaskHolidayActivity01Service {

	@Autowired
	@Qualifier("HolidayActivity01Repo")
	private Repo<HolidayActivity01, Long> holidayActivity01Repo;

	@Override
	public List<HolidayActivity01> listAllActivity() {
		return holidayActivity01Repo.find("$taskHolidayActivityGetAll", Params.param("now", new Date())).list();
	}

}
