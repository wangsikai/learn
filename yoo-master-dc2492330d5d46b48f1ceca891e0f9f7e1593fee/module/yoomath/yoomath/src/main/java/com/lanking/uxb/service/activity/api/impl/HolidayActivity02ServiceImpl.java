package com.lanking.uxb.service.activity.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Cfg;
import com.lanking.uxb.service.activity.api.HolidayActivity02Service;

import httl.util.CollectionUtils;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity02ServiceImpl implements HolidayActivity02Service {
	@Autowired
	@Qualifier("HolidayActivity02Repo")
	private Repo<HolidayActivity02, Long> repo;


	@Override
	public Map<String, Object> getActivityCfg(long code) {
		HolidayActivity02 hActive = get(code);
		Map<String, Object> data = new HashMap<String, Object>();
		if (hActive != null) {
			data.put("startTime", hActive.getStartTime());
			data.put("endTime", hActive.getEndTime());
		}
		HolidayActivity02Cfg cfg = hActive.getCfg();
		if (cfg != null) {
			if(CollectionUtils.isNotEmpty(cfg.getPhases())){
				data.put("phase1Start", cfg.getPhases().get(0).get(0));
				data.put("phase1End", cfg.getPhases().get(0).get(1));
				data.put("phase2Start", cfg.getPhases().get(1).get(0));
				data.put("phase2End", cfg.getPhases().get(1).get(1));
				data.put("phase3Start", cfg.getPhases().get(2).get(0));
				data.put("phase3End", cfg.getPhases().get(2).get(1));
				data.put("phase4Start", cfg.getPhases().get(3).get(0));
				data.put("phase4End", cfg.getPhases().get(3).get(1));
			}
			
		}
		return data;
	}

	@Override
	public HolidayActivity02 get(long code) {
		return repo.get(code);
	}
}
