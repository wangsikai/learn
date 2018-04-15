package com.lanking.uxb.service.honor.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.api.UserHonorService;

@Transactional(readOnly = true)
@Service
public class UserHonorServiceImpl implements UserHonorService {
	@Autowired
	@Qualifier("UserHonorRepo")
	private Repo<UserHonor, Long> userHonorRepo;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;

	@Transactional
	@Override
	public void init(long userId) {
		UserHonor honor = new UserHonor();
		honor.setUserId(userId);
		honor.setCreateAt(new Date());
		honor.setGrowth(0);
		honor.setLevel(1);
		honor.setPoint(0);
		honor.setUpdateAt(honor.getCreateAt());
		honor.setUpgrade(false);
		honor.setCoins(0);
		userHonorRepo.save(honor);
	}

	@Override
	public UserHonor getUserHonor(long userId) {
		return userHonorRepo.get(userId);
	}

	@Transactional
	@Override
	public UserHonor save(UserHonor userHonor) {

		return userHonorRepo.save(userHonor);
	}
	
	@Transactional
	@Override
	public void uptUserHonor(long userId, boolean isupGrade) {
		userHonorRepo.execute("$uptUserHonorGrade", Params.param("isupGrade", isupGrade).put("userId", userId));
	}

	@Async
	@Transactional
	@Override
	public void asyncUptUserHonor(GrowthAction growthAction, CoinsAction coinsAction, Long userId, Biz biz,
			long bizId) {
		growthService.grow(growthAction, userId, -1, biz, bizId, false);
		coinsService.earn(coinsAction, userId, -1, biz, bizId);
	}

	@Override
	@Transactional
	public void saveOrUpdate(Long userId) {
		UserHonor userHonor = userHonorRepo.get(userId);
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(userId);
			userHonor.setCoins(20);
			userHonor.setGrowth(0);
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());

		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setCoins(userHonor.getCoins() + 20);
		}

		userHonorRepo.save(userHonor);
	}
}
