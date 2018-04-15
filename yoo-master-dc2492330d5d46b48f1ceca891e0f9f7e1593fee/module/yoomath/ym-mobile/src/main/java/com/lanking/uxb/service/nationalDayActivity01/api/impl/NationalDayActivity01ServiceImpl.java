package com.lanking.uxb.service.nationalDayActivity01.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01Service;

@Transactional(readOnly = true)
@Service
public class NationalDayActivity01ServiceImpl implements NationalDayActivity01Service {

	@Autowired
	@Qualifier("NationalDayActivity01Repo")
	private Repo<NationalDayActivity01, Long> nationalDayActivity01Repo;

	/**
	 * 活动code
	 */
	public static Long NATIONAL_DAY_ACTIVITY_ID = 795646319942180866L;

	@Override
	public NationalDayActivity01 getActivity() {
		return nationalDayActivity01Repo.get(NATIONAL_DAY_ACTIVITY_ID);
	}

}
