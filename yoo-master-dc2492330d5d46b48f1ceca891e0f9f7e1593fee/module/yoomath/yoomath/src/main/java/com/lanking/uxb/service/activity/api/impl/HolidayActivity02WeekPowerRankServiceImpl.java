package com.lanking.uxb.service.activity.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02WeekPowerRank;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity02Service;
import com.lanking.uxb.service.activity.api.HolidayActivity02WeekPowerRankService;
import com.lanking.uxb.service.activity.value.HolidayActivity02RetRankInfo;

import httl.util.CollectionUtils;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity02WeekPowerRankServiceImpl implements HolidayActivity02WeekPowerRankService {
	@Autowired
	@Qualifier("HolidayActivity02WeekPowerRankRepo")
	private Repo<HolidayActivity02WeekPowerRank, Long> repo;
	@Autowired
	private HolidayActivity02Service holidayActivity02Service;

	@Override
	@Transactional
	public void addActivity02WeekPowerRank(long activityCode, long userId, Integer power,Date startTime,Date endTime) {
		HolidayActivity02WeekPowerRank holidayActivity02WeekPowerRank = new HolidayActivity02WeekPowerRank();
		
		holidayActivity02WeekPowerRank.setActivityCode(activityCode);
		holidayActivity02WeekPowerRank.setUserId(userId);
		holidayActivity02WeekPowerRank.setStartTime(startTime);
		holidayActivity02WeekPowerRank.setEndTime(endTime);
		holidayActivity02WeekPowerRank.setRealWeekPower(power);
		holidayActivity02WeekPowerRank.setWeekPower(power);
		
		repo.save(holidayActivity02WeekPowerRank);
	}

	@Override
	public HolidayActivity02WeekPowerRank getActivity02WeekPowerRank(long activityCode, long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		params.put("time", new Date());
		HolidayActivity02WeekPowerRank weekPowerRank = repo.find("$getWeekPowerRank", params).get();
		
		return weekPowerRank;
	}

	@Override
	@Transactional
	public void updateActivity02WeekPowerRank(HolidayActivity02WeekPowerRank holidayActivity02WeekPowerRank) {
		repo.save(holidayActivity02WeekPowerRank);
	}

	@Override
	public List<HolidayActivity02RetRankInfo> getActivity02WeekPowerRanks(long activityCode, long userId,
			HolidayActivity02RetRankInfo me) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		params.put("time", new Date());
		Map weekPowerMap = repo.find("$getUserWeekPowerRank", params).get(Map.class);
		if(weekPowerMap != null) {
			if(weekPowerMap.get("power") != null){
				Long power = Long.parseLong(weekPowerMap.get("power").toString());
				me.setPower(power);
			}
			if(weekPowerMap.get("user_id") != null){
				Long tempUserId = Long.parseLong(weekPowerMap.get("user_id").toString());
				me.setUserId(tempUserId);
			}
			if(weekPowerMap.get("rank") != null){
				Integer rank = Integer.parseInt((weekPowerMap.get("rank").toString()));
				me.setRank(rank);
			}
			if(weekPowerMap.get("total") != null){
				Integer total = Integer.parseInt((weekPowerMap.get("total").toString()));
				me.setTotal(total);
			}
			if(weekPowerMap.get("activity_user_code") != null){
				Long tempUserCode = Long.parseLong(weekPowerMap.get("activity_user_code").toString());
				me.setUserActivityCode(tempUserCode);
			}
		}
		
		params = Params.param("code", activityCode);
		params.put("time", new Date());
		
		List<Map> weekPowerMaps = repo.find("$getUserWeekPowerRanks", params).list(Map.class);
		List<HolidayActivity02RetRankInfo> infos = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(weekPowerMaps)) {
			for(Map map:weekPowerMaps){
				HolidayActivity02RetRankInfo userInfo = new HolidayActivity02RetRankInfo();
				if(map.get("power") != null){
					Long power = Long.parseLong(map.get("power").toString());
					userInfo.setPower(power);
				}
				if(map.get("user_id") != null){
					Long tempUserId = Long.parseLong(map.get("user_id").toString());
					userInfo.setUserId(tempUserId);
				}
				if(map.get("rank") != null){
					Integer rank = Integer.parseInt((map.get("rank").toString()));
					userInfo.setRank(rank);
				}
				if(map.get("activity_user_code") != null){
					Long userActivityCode = Long.parseLong(map.get("activity_user_code").toString());
					userInfo.setUserActivityCode(userActivityCode);
				}
				if(map.get("total") != null){
					Integer total = Integer.parseInt((map.get("total").toString()));
					userInfo.setTotal(total);
				}
				
				infos.add(userInfo);
			}
		}
		
		return infos;
	}

	@Override
	@Transactional
	public void updateUserWeekPower(Long code, Long userId, Integer difference) {
		HolidayActivity02WeekPowerRank weekPower = this.getActivity02WeekPowerRank(code, userId);
		// 不存在周记录直接创建
		if (weekPower == null) {
			HolidayActivity02 activity = holidayActivity02Service.get(code);
			Date phaseStart = null;
			Date phaseEnd = null;
			
			HolidayActivity02Cfg cfg = activity.getCfg();
			if (cfg != null) {
				List<List<Date>> phases = cfg.getPhases();
				List<Date> currentPhase = null;
				Date currentTime = new Date();
				for (List<Date> phase : phases) {
					if (currentTime.after(phase.get(0)) && currentTime.before(phase.get(1))) {
						currentPhase = phase;
						break;
					}
				}

				if (currentPhase != null) {
					phaseStart = currentPhase.get(0);
					phaseEnd = currentPhase.get(1);
				}
				
				this.addActivity02WeekPowerRank(code, userId, difference, phaseStart, phaseEnd);
			}
		} else {
			// 存在记录更新记录
			Params params = Params.param();
			params.put("code", code);
			params.put("userId", userId);
			params.put("time", new Date());
			params.put("difference", difference);
			repo.execute("$updateUserWeekPower", params);
		}
	}	
}
