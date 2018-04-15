package com.lanking.uxb.service.session.api.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.SessionUserService;

public abstract class AbstractSessionUserService implements SessionUserService {

	@Autowired
	private SessionService sessionService;

	@Override
	public void handleLogin() {
		SessionPacket sp = new SessionPacket();
		sp.setUserType(getLoginUserType());
		sessionService.refreshCurrentSession(sp, false);
	}

}
