package com.lanking.uxb.zycon.operation.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycActivityEntranceCfgService;

@Transactional(readOnly = true)
@Service
public class ZycActivityEntranceCfgServiceImpl implements ZycActivityEntranceCfgService {

	@Autowired
	@Qualifier("ActivityEntranceCfgRepo")
	private Repo<ActivityEntranceCfg, Long> actEntranceCfgRepo;

	@Override
	public ActivityEntranceCfg findByApp(YooApp app) {
		ActivityEntranceCfg cfg = actEntranceCfgRepo.find("$zycFindByApp", Params.param("app", app.getValue())).get();
		if (cfg == null) {
			cfg = new ActivityEntranceCfg();
			cfg.setApp(app);
			cfg.setStatus(Status.DISABLED);
		}
		return cfg;
	}

	@Transactional
	@Override
	public ActivityEntranceCfg updateStatus(YooApp app, Status status, long userId) {
		ActivityEntranceCfg cfg = findByApp(app);
		if (cfg == null) {
			cfg = new ActivityEntranceCfg();
			cfg.setApp(app);
			cfg.setCreateAt(new Date());
			cfg.setCreateId(userId);
			cfg.setUpdateAt(cfg.getCreateAt());
			cfg.setUpdateId(userId);
		} else {
			cfg.setStatus(status);
			cfg.setUpdateAt(new Date());
			cfg.setUpdateId(userId);
		}
		cfg.setStatus(status);
		return actEntranceCfgRepo.save(cfg);
	}

	@Transactional
	@Override
	public ActivityEntranceCfg update(YooApp app, Status status, Long icon, String uri, long userId) {
		ActivityEntranceCfg cfg = findByApp(app);
		if (cfg == null) {
			cfg = new ActivityEntranceCfg();
			cfg.setApp(app);
			cfg.setCreateAt(new Date());
			cfg.setCreateId(userId);
			cfg.setUpdateAt(cfg.getCreateAt());
			cfg.setUpdateId(userId);
		} else {
			cfg.setStatus(status);
			cfg.setUpdateAt(new Date());
			cfg.setUpdateId(userId);
		}
		cfg.setStatus(status);
		cfg.setIcon(icon);
		cfg.setUri(uri);
		return actEntranceCfgRepo.save(cfg);
	}
}
