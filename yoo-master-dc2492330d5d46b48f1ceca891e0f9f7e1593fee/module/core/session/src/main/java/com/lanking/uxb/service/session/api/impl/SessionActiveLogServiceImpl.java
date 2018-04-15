package com.lanking.uxb.service.session.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.SessionActiveLog;
import com.lanking.uxb.service.session.api.SessionActiveLogService;

@Transactional(readOnly = true)
@Service
public class SessionActiveLogServiceImpl implements SessionActiveLogService {

	@Autowired
	@Qualifier("SessionActiveLogRepo")
	private Repo<SessionActiveLog, Long> sessionActiveLogRepo;

	@Transactional
	@Override
	public void log(String token, long userId, DeviceType deviceType, long startActiveAt, long endActiveAt) {
		SessionActiveLog log = new SessionActiveLog();
		log.setActiveTime(endActiveAt - startActiveAt);
		log.setCreateAt(System.currentTimeMillis());
		log.setEndActiveAt(endActiveAt);
		log.setStartActiveAt(startActiveAt);
		log.setToken(token);
		log.setUserId(userId);
		log.setDeviceType(deviceType);
		sessionActiveLogRepo.save(log);
	}

}
