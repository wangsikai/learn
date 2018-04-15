package com.lanking.uxb.service.activity.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivity;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.LotteryActivityService;

@Transactional(readOnly = true)
@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {

	@Autowired
	@Qualifier("LotteryActivityRepo")
	private Repo<LotteryActivity, Long> lotteryActivityRepo;

	@Override
	public LotteryActivity get(long code) {
		return lotteryActivityRepo.find("$get", Params.param("code", code)).get();
	}

	@Override
	@Transactional
	public void save(LotteryActivity lotteryActivity) {
		lotteryActivityRepo.save(lotteryActivity);
	}
}
