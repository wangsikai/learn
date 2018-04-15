package com.lanking.uxb.service.activity.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01User;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserService;

/**
 * 假期活动01参与活动的用户接口实现
 * 
 * @author peng.zhao
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01UserServiceImpl implements HolidayActivity01UserService {

	@Autowired
	@Qualifier("HolidayActivity01UserRepo")
	private Repo<HolidayActivity01User, Long> holidayActivity01UserRepo;

	@Override
	public HolidayActivity01User getByUserId(long code, long userId) {
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		return holidayActivity01UserRepo.find("$findByUserId", params).get();
	}

	@Transactional
	@Override
	public void resetUserNewLuckyDraw(long activityCode, long userId) {
		// 不要直接取user，再塞值，需要控制其他属性值并发的问题
		holidayActivity01UserRepo.execute("$resetUserNewLuckyDraw",
				Params.param("userId", userId).put("activityCode", activityCode));
	}

	@Transactional
	@Override
	public void checkHolidayActivity01User(long activityCode, long userId) {
		HolidayActivity01User user = this.getByUserId(activityCode, userId);
		if (user == null) {
			user = new HolidayActivity01User();
			user.setActivityCode(activityCode);
			user.setCostLuckyDraw(0);
			user.setCreateAt(new Date());
			user.setLuckyDraw(0);
			user.setNewLuckyDraw(0);
			user.setUserId(userId);
			holidayActivity01UserRepo.save(user);
		}
	}

	// 此处已不需要再检查user是否存在
	@Override
	@Transactional
	public void addUserLuckyDraw(long activityCode, long userId, int count, boolean isPassive) {

		// 直接通过SQL进行数据更新
		Params params = Params.param("activityCode", activityCode).put("userId", userId).put("num", count);

		// 是否消费抽奖次数
		boolean isCost = count < 0 ? true : false;

		// 是否被动新增抽奖次数
		boolean isNew = (count > 0 && isPassive) ? true : false;

		params.put("isCost", isCost).put("isNew", isNew);
		holidayActivity01UserRepo.execute("$addUserLuckyDraw", params);
	}
}
