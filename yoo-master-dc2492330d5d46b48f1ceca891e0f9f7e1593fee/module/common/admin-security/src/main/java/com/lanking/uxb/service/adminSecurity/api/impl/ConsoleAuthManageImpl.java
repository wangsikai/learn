package com.lanking.uxb.service.adminSecurity.api.impl;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleAuthManage;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ConsoleAuthManageImpl implements ConsoleAuthManage {
	@Autowired
	@Qualifier("ConsoleUserRepo")
	private Repo<ConsoleUser, Long> repo;

	@Autowired
	private SessionService sessionService;

	@Override
	public void handleLogin(ConsoleUser consoleUser, HttpServletRequest request, HttpServletResponse response) {
		SessionPacket sp = new SessionPacket();
		sp.setUserType(getLoginUserType());
		sp.setUserId(consoleUser.getId());
		WebUtils.addCookie(request, response, Cookies.REMEMBER_USERTYPE, getLoginUserType().toString());
		sessionService.refreshCurrentSession(sp, false);
	}

	@Override
	public UserType getLoginUserType() {
		return UserType.NULL;
	}

	@Override
	public Userable getUser(long id) {
		return repo.get(id);
	}

	@Override
	public Set<Long> getRoles(long id) {
		return Sets.newHashSet();
	}

	@Override
	public void handleLogin() {
		SessionPacket sp = new SessionPacket();
		sp.setUserType(getLoginUserType());
		sessionService.refreshCurrentSession(sp, false);
	}

}
