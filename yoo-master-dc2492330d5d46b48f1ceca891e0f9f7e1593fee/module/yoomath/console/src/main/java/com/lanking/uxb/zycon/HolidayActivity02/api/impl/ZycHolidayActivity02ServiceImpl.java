package com.lanking.uxb.zycon.HolidayActivity02.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02Service;

@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity02ServiceImpl implements ZycHolidayActivity02Service {

	@Autowired
	@Qualifier("HolidayActivity02Repo")
	private Repo<HolidayActivity02, Long> repo;
	
	@Override
	public HolidayActivity02 get(long activityCode) {
		return repo.get(activityCode);
	}

}
