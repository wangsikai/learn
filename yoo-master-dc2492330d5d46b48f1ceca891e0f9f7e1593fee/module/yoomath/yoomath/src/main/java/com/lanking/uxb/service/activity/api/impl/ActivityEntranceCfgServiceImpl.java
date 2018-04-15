package com.lanking.uxb.service.activity.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.ActivityEntranceCfgService;

@Transactional(readOnly = true)
@Service
public class ActivityEntranceCfgServiceImpl implements ActivityEntranceCfgService {

	@Autowired
	@Qualifier("ActivityEntranceCfgRepo")
	private Repo<ActivityEntranceCfg, Long> actEntranceCfgRepo;

	@Override
	public ActivityEntranceCfg findByApp(YooApp app) {
		return actEntranceCfgRepo.find("$findByApp", Params.param("app", app.getValue())).get();
	}

}
