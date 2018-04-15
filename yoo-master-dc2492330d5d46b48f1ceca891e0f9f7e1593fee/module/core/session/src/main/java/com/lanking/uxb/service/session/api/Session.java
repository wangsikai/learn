package com.lanking.uxb.service.session.api;

import java.util.Set;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.SessionStatus;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.uxb.service.session.api.impl.AttrSession;

public interface Session {

	com.lanking.cloud.domain.base.session.Session getInternalSession();

	long getId();

	String getToken();

	SessionStatus getStatus();

	long getAccountId();

	Userable getUser();

	long getUserId();

	boolean isLogin();

	boolean isActive();

	boolean isClientUser();

	boolean isAdmin();

	UserType getUserType();

	Set<Long> getRoles();

	DeviceType getDeviceType();

	String getVersion();

	AttrSession getAttrSession();

	Product getLoginSource();
}
