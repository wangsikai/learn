package com.lanking.uxb.service.holidayActivity02.api.impl;

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
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02WeekPowerRank;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.holidayActivity02.api.TaskHolidayActivity02WeekPowerRankService;

import httl.util.CollectionUtils;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskHolidayActivity02WeekPowerRankServiceImpl implements TaskHolidayActivity02WeekPowerRankService {
	@Autowired
	@Qualifier("HolidayActivity02WeekPowerRankRepo")
	private Repo<HolidayActivity02WeekPowerRank, Long> repo;

	@Override
	@Transactional
	public void processRank(HolidayActivity02 activity) {
		Params params = Params.param("code", activity.getCode());
		params.put("time", new Date());
		
		List<Map> weekPowerMaps = repo.find("$getUserWeekPowerInfos", params).list(Map.class);
		List<HolidayActivity02WeekPowerRank> infos = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(weekPowerMaps)) {
			Integer rank = 1;
			for(Map map:weekPowerMaps){
				HolidayActivity02WeekPowerRank userInfo = new HolidayActivity02WeekPowerRank();
				Long tempId = ((BigInteger) map.get("id")).longValue();
				userInfo.setId(tempId);
				userInfo.setRank0(rank);
				
				infos.add(userInfo);
				
				rank++;
			}
			
			for(HolidayActivity02WeekPowerRank weekRank:infos){
				params = Params.param("id", weekRank.getId());
				params.put("rank0", weekRank.getRank0());
				repo.find("$updateUserWeekPowerRanks", params).execute();
			}
		}
	}

}
