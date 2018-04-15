package com.lanking.uxb.service.session.api.impl;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.SessionStatus;
import com.lanking.cloud.domain.base.session.api.SessionConstants;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.service.session.api.Session;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.ex.SessionException;

public abstract class AbstractSession implements Session {

	private String token;
	private SessionPacket packet;

	public AbstractSession(String token) {
		this.token = token;
	}

	@Override
	public long getId() {
		return getPacket().getId();
	}

	@Override
	public String getToken() {
		return token;
	}

	void setToken(String token) {
		this.token = token;
	}

	@Override
	public SessionStatus getStatus() {
		return getPacket().getStatus();
	}

	@Override
	public long getUserId() {
		return getPacket().getUserId();
	}

	@Override
	public long getAccountId() {
		return getPacket().getAccountId();
	}

	@Override
	public boolean isLogin() {
		return getPacket().getUserId() != SessionConstants.GUEST_ID
				&& (getPacket().getStatus() == SessionStatus.ACTIVE || getPacket().getStatus() == SessionStatus.UNACTIVE);
	}

	@Override
	public boolean isActive() {
		return getPacket().getStatus() == SessionStatus.ACTIVE;
	}

	@Override
	public boolean isClientUser() {
		return getPacket().getUserType() == UserType.TEACHER || getPacket().getUserType() == UserType.STUDENT
				|| getPacket().getUserType() == UserType.PARENT;
	}

	@Override
	public boolean isAdmin() {
		return getPacket().getUserType() == UserType.ADMIN;
	}

	public SessionPacket getPacket() {
		if (packet == null) {
			packet = initPacket();
			if (packet == null) {
				throw new SessionException(SessionException.SESSION_INVALID);
			}
		}
		return packet;
	}

	@Override
	public UserType getUserType() {
		return getPacket().getUserType();
	}

	@Override
	public DeviceType getDeviceType() {
		return getPacket().getDeviceType();
	}

	@Override
	public String getVersion() {
		return getPacket().getVersion();
	}

	@Override
	public AttrSession getAttrSession() {
		return getPacket().getAttrSession();
	}

	@Override
	public Product getLoginSource() {
		return getPacket().getLoginSource();
	}

	protected abstract SessionPacket initPacket();

}
