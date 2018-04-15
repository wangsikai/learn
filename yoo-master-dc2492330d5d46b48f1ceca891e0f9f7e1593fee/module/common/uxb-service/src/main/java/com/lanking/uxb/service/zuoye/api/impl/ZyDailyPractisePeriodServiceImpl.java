package com.lanking.uxb.service.zuoye.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractisePeriod;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractisePeriodService;

/**
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ZyDailyPractisePeriodServiceImpl implements ZyDailyPractisePeriodService {
	@Autowired
	@Qualifier("DailyPractisePeriodRepo")
	Repo<DailyPractisePeriod, Long> repo;

	@Override
	@Transactional
	public DailyPractisePeriod save(long sectionCode, int period) {
		DailyPractisePeriod practisePeriod = new DailyPractisePeriod();
		practisePeriod.setPeriod(period);
		practisePeriod.setSectionCode(sectionCode);

		return repo.save(practisePeriod);
	}

	@Override
	public DailyPractisePeriod findBySectionCode(long sectionCode) {
		return repo.find("$zyFindBySectionCode", Params.param("sectionCode", sectionCode)).get();
	}
}
