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
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRank;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity02PowerRankService;
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
public class HolidayActivity02PowerRankServiceImpl implements HolidayActivity02PowerRankService {
	@Autowired
	@Qualifier("HolidayActivity02PowerRankRepo")
	private Repo<HolidayActivity02PowerRank, Long> repo;

	@Override
	@Transactional
	public void addActivity02PowerRank(long activityCode, long userId, Integer power) {
		HolidayActivity02PowerRank holidayActivity02PowerRank = new HolidayActivity02PowerRank();
		
		holidayActivity02PowerRank.setActivityCode(activityCode);
		holidayActivity02PowerRank.setUserId(userId);
		holidayActivity02PowerRank.setPower(power);
		holidayActivity02PowerRank.setRealPower(power);
		
		repo.save(holidayActivity02PowerRank);
		
	}

	@Override
	public HolidayActivity02PowerRank getActivity02PowerRank(long activityCode, long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		HolidayActivity02PowerRank powerRank = repo.find("$getPowerRank", params).get();
		
		return powerRank;
	}

	@Override
	@Transactional
	public void updateActivity02PowerRank(HolidayActivity02PowerRank holidayActivity02PowerRank) {
		repo.save(holidayActivity02PowerRank);
	}

	@Override
	public List<HolidayActivity02RetRankInfo> getActivity02PowerRanks(long activityCode, long userId,
			HolidayActivity02RetRankInfo me) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		params.put("time", new Date());
		Map powerMap = repo.find("$getUserPowerRank", params).get(Map.class);
		if(powerMap != null) {
			if(powerMap.get("power") != null){
				Long power = Long.parseLong(powerMap.get("power").toString());
				me.setPower(power);
			}
			if(powerMap.get("user_id") != null){
				Long tempUserId = Long.parseLong(powerMap.get("user_id").toString());
				me.setUserId(tempUserId);
			}
			if(powerMap.get("rank") != null){
				Integer rank = Integer.parseInt((powerMap.get("rank").toString()));
				me.setRank(rank);
			}
			if(powerMap.get("total") != null){
				Integer total = Integer.parseInt((powerMap.get("total").toString()));
				me.setTotal(total);
			}
			if(powerMap.get("activity_user_code") != null){
				Long tempUserCode = Long.parseLong(powerMap.get("activity_user_code").toString());
				me.setUserActivityCode(tempUserCode);
			}
		}
		
		params = Params.param("code", activityCode);
		
		List<Map> powerMaps = repo.find("$getUserPowerRanks", params).list(Map.class);
		List<HolidayActivity02RetRankInfo> infos = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(powerMaps)) {
			for(Map map:powerMaps){
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
	public void updateUserPower(Long code, Long userId, Integer difference) {
		Params params = Params.param();
		params.put("userId", userId);
		params.put("code", code);
		params.put("difference", difference);
		repo.execute("$updateUserPower", params);
	}
}
