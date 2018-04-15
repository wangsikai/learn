package com.lanking.uxb.service.holidayActivity02.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRank;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.holidayActivity02.api.TaskHolidayActivity02PowerRankService;

import httl.util.CollectionUtils;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskHolidayActivity02PowerRankServiceImpl implements TaskHolidayActivity02PowerRankService {
	@Autowired
	@Qualifier("HolidayActivity02PowerRankRepo")
	private Repo<HolidayActivity02PowerRank, Long> repo;

	@Override
	@Transactional
	public void processRank(HolidayActivity02 activity) {
		Params params = Params.param("code", activity.getCode());

		List<Map> powerMaps = repo.find("$getUserPowerInfos", params).list(Map.class);
		List<HolidayActivity02PowerRank> infos = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(powerMaps)) {
			Integer rank = 1;
			for(Map map:powerMaps){
				HolidayActivity02PowerRank userInfo = new HolidayActivity02PowerRank();
				Long tempId = ((BigInteger) map.get("id")).longValue();
				userInfo.setId(tempId);
				userInfo.setRank0(rank);
				
				infos.add(userInfo);
				
				rank++;
				
				infos.add(userInfo);
			}
			
			
			for(HolidayActivity02PowerRank powerRank:infos){
				params = Params.param("id", powerRank.getId());
				params.put("rank0", powerRank.getRank0());
				repo.find("$updateUserPowerRanks", params).execute();
			}
		}
	}

}
