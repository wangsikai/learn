package com.lanking.uxb.zycon.HolidayActivity02.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRank;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRecord;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02WeekPowerRank;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02PowerRankService;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02UserService;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02WeekPowerRankService;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity02WeekPowerRankServiceImpl implements ZycHolidayActivity02WeekPowerRankService {

	@Autowired
	@Qualifier("HolidayActivity02WeekPowerRankRepo")
	private Repo<HolidayActivity02WeekPowerRank, Long> repo;
	@Autowired
	private ZycHolidayActivity02PowerRankService holidayActivity02PowerRankService;
	@Autowired
	@Qualifier("HolidayActivity02PowerRankRepo")
	private Repo<HolidayActivity02PowerRank, Long> rankRepo;
	@Autowired
	@Qualifier("HolidayActivity02PowerRecordRepo")
	private Repo<HolidayActivity02PowerRecord, Long> recordRepo;
	@Autowired
	@Qualifier("HolidayActivity02UserRepo")
	private Repo<HolidayActivity02User, Long> userRepo;
	@Autowired
	private ZycHolidayActivity02UserService holidayActivity02UserService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryWeekPowerRankByPhase(List<Date> phase, Long activityCode, Pageable p) {
		Params params = Params.param();
		params.put("activityCode", activityCode);
		
		if (CollectionUtils.isNotEmpty(phase) && phase.size() > 1) {
			params.put("startTime", phase.get(0));
			params.put("endTime", phase.get(1));
		}
		return repo.find("$queryWeekPowerRankByPhase", params).fetch(p, Map.class);
	}

	@Override
	@Transactional
	public void updateWeekPower(Long activityCode, Long id, Integer power, Long userId) {
		HolidayActivity02WeekPowerRank weekRank = repo.get(id);
		if (weekRank == null) {
			return;
		}
		HolidayActivity02PowerRank rank = holidayActivity02PowerRankService.getByUser(activityCode, weekRank.getUserId());
		if (rank == null) {
			return;
		}
		HolidayActivity02User user = holidayActivity02UserService.getUserActivityInfo(activityCode, weekRank.getUserId());
		if (user == null) {
			return;
		}
		
		int difference = power - weekRank.getWeekPower();
		int newPower = rank.getPower() + difference;
		
		// 更新周表
		weekRank.setWeekPower(power);
		repo.save(weekRank);
		// 更新总战力表
		rank.setPower(newPower);
		rankRepo.save(rank);
		// 更新用户表
		user.setPower(newPower);
		userRepo.save(user);
		// 更新日志表
		HolidayActivity02PowerRecord record = new HolidayActivity02PowerRecord();
		record.setActivityCode(activityCode);
		record.setPower(power);
		record.setRankId(weekRank.getId());
		record.setType(0);
		record.setUserId(userId);
		recordRepo.save(record);
	}

}
