package com.lanking.uxb.zycon.HolidayActivity02.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRank;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRecord;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02PowerRankService;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02UserService;

@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity02PowerRankServiceImpl implements ZycHolidayActivity02PowerRankService {

	@Autowired
	@Qualifier("HolidayActivity02PowerRankRepo")
	private Repo<HolidayActivity02PowerRank, Long> repo;
	@Autowired
	@Qualifier("HolidayActivity02PowerRecordRepo")
	private Repo<HolidayActivity02PowerRecord, Long> recordRepo;
	@Autowired
	@Qualifier("HolidayActivity02UserRepo")
	private Repo<HolidayActivity02User, Long> userRepo;
	@Autowired
	private ZycHolidayActivity02UserService holidayActivity02UserService;
	
	@Override
	public HolidayActivity02PowerRank getByUser(Long activityCode, Long userId) {
		Params params = Params.param();
		params.put("activityCode", activityCode);
		params.put("userId", userId);
		
		return repo.find("$queryByUser", params).get();
	}

	@Override
	@Transactional
	public void updateByPower(Long id, Integer power, Long userId) {
		HolidayActivity02PowerRank rank = repo.get(id);
		if (rank == null) {
			return;
		}
		HolidayActivity02User user = holidayActivity02UserService.getUserActivityInfo(rank.getActivityCode(), rank.getUserId());
		if (user == null) {
			return;
		}
		
		// 更新rank
		rank.setPower(power);
		repo.save(rank);
		// 更新用户表
		user.setPower(power);
		userRepo.save(user);
		// 更新日志表
		HolidayActivity02PowerRecord record = new HolidayActivity02PowerRecord();
		record.setActivityCode(rank.getActivityCode());
		record.setPower(power);
		record.setRankId(rank.getId());
		record.setType(1);
		record.setUserId(userId);
		recordRepo.save(record);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryPowerRankList(Long activityCode, Pageable p) {
		Params params = Params.param();
		params.put("activityCode", activityCode);
		return repo.find("$queryPowerRankList", params).fetch(p, Map.class);
	}
}
