package com.lanking.uxb.service.activity.api.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Medal;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity02MedalService;
import com.lanking.uxb.service.activity.api.HolidayActivity02PowerRankService;
import com.lanking.uxb.service.activity.api.HolidayActivity02UserService;
import com.lanking.uxb.service.activity.api.HolidayActivity02WeekPowerRankService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity02MedalServiceImpl implements HolidayActivity02MedalService {
	@Autowired
	@Qualifier("HolidayActivity02MedalRepo")
	private Repo<HolidayActivity02Medal, Long> repo;
	@Autowired
	private HolidayActivity02UserService holidayActivity02UserService;
	@Autowired
	private HolidayActivity02WeekPowerRankService holidayActivity02WeekPowerRankService;
	@Autowired
	private HolidayActivity02PowerRankService holidayActivity02PowerRankService;
	
	private Logger logger = LoggerFactory.getLogger(HolidayActivity02MedalServiceImpl.class);

	@Override
	public List<HolidayActivity02Medal> getNotReceivedMedals(Long activityCode, Long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		List<HolidayActivity02Medal> medals = repo.find("$findNotReceivedMedals", params).list();
		
		return medals;
	}

	@Override
	public List<HolidayActivity02Medal> getMedals(Long activityCode, Long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		List<HolidayActivity02Medal> medals = repo.find("$findAllMedals", params).list();
		
		return medals;
	}

	@Override
	public HolidayActivity02Medal get(Long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public HolidayActivity02Medal receiveMedal(Long id, Long code, Integer difference, Long userId) {
		// 业务校验
		HolidayActivity02Medal medal = repo.get(id);
		if (medal == null || medal.getActivityCode() != code.longValue() || medal.getUserId() != userId.longValue()
				|| medal.getAchieved() != 1 || medal.getReceived() == 1) {
			return null;
		}
		
		// 更新HolidayActivity02Medal
		medal.setPower(difference);
		medal.setReceived(1);
		// 添加更新时间日志
		logger.error("update HolidayActivity02Medal start:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		repo.save(medal);
		logger.error("update HolidayActivity02Medal end:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		// 更新到HolidayActivity02User表
		logger.error("update HolidayActivity02User start:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		holidayActivity02UserService.updateUserPower(code, userId, difference);
		logger.error("update HolidayActivity02User end:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		// 更新到HolidayActivity02WeekPowerRank表,注意weekPower和realWeekPower都要更新
		logger.error("update HolidayActivity02WeekPowerRank start:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		holidayActivity02WeekPowerRankService.updateUserWeekPower(code, userId, difference);
		logger.error("update HolidayActivity02WeekPowerRank end:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		// 更新到HolidayActivity02PowerRank表,注意power和realPower都要更新
		logger.error("update HolidayActivity02PowerRank start:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		holidayActivity02PowerRankService.updateUserPower(code, userId, difference);
		logger.error("update HolidayActivity02PowerRank end:" + System.currentTimeMillis() + " userid " + Security.getUserId());
		
		return medal;
	}
	
}
