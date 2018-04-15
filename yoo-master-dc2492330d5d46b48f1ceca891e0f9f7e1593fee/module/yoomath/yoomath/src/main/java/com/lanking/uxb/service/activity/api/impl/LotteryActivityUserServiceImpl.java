package com.lanking.uxb.service.activity.api.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivityUser;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityVirtualCoinsType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.LotteryActivityUserService;

/**
 * 活动用户数据接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月23日
 */
@Service
@Transactional(readOnly = true)
public class LotteryActivityUserServiceImpl implements LotteryActivityUserService {
	@Autowired
	@Qualifier("LotteryActivityUserRepo")
	private Repo<LotteryActivityUser, Long> repo;

	@Override
	public LotteryActivityUser get(long activityCode, long userId) {
		return repo.find("$getLotteryActivityUser", Params.param("activityCode", activityCode).put("userId", userId))
				.get();
	}

	@Override
	@Transactional
	public void updateTotalCoins(long activityCode, long userId, int totalCoins) {
		LotteryActivityUser lau = this.get(activityCode, userId);
		if (lau == null) {
			lau = this.create(activityCode, userId, totalCoins);
		}
		lau.setTotalCoins(totalCoins);
		repo.save(lau);
	}

	@Override
	@Transactional
	public void updateLastIncrRecordAt(long activityCode, long userId, Date date) {
		LotteryActivityUser lau = this.get(activityCode, userId);
		if (lau != null) {
			lau.setLastIncrRecordAt(date);
			repo.save(lau);
		}
	}

	@Override
	@Transactional
	public LotteryActivityUser create(long activityCode, long userId, int totalCoins) {
		LotteryActivityUser lau = this.get(activityCode, userId);
		if (lau == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -1);
			lau = new LotteryActivityUser();
			lau.setActivityCode(activityCode);
			lau.setLastIncrRecordAt(cal.getTime());
			lau.setTotalCoins(totalCoins);
			lau.setUserId(userId);
			lau.setVirtualCoinsType(LotteryActivityVirtualCoinsType.CUSTOM);
		}
		return lau;
	}
}
