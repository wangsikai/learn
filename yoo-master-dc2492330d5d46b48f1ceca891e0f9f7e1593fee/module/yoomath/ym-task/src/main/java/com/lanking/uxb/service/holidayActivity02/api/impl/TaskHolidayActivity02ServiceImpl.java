package com.lanking.uxb.service.holidayActivity02.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.uxb.service.holidayActivity02.api.TaskHolidayActivity02Service;

@Transactional(readOnly = true)
@Service
public class TaskHolidayActivity02ServiceImpl implements TaskHolidayActivity02Service {

	@Autowired
	@Qualifier("HolidayActivity02Repo")
	private Repo<HolidayActivity02, Long> repo;

	@Transactional(readOnly = true)
	@Override
	public HolidayActivity02 get(long code) {
		return repo.get(code);
	}

	@Transactional(readOnly = true)
	@Override
	public List<HolidayActivity02> listAllProcessingActivity() {
		return repo.find("$TaskQueryActivity").list();
	}

}
