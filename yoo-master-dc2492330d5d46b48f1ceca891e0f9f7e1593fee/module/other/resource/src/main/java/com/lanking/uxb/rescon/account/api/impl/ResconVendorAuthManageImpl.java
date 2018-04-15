package com.lanking.uxb.rescon.account.api.impl;

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
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatus;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.rescon.account.api.ResconVendorAuthManage;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ResconVendorAuthManageImpl implements ResconVendorAuthManage {

	@Autowired
	@Qualifier("VendorUserRepo")
	Repo<VendorUser, Long> vendorUserRepo;

	@Autowired
	private SessionService sessionService;

	@Override
	public UserType getLoginUserType() {
		return UserType.NULL;
	}

	@Override
	public Userable getUser(long id) {
		return vendorUserRepo.get(id);
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

	@Transactional
	@Override
	public void handleLogin(VendorUser vendorUser, HttpServletRequest request, HttpServletResponse response) {
		SessionPacket p = new SessionPacket();
		p.setUserType(UserType.findByValue(vendorUser.getType().getValue()));
		p.setUserId(vendorUser.getId());
		WebUtils.addCookie(request, response, Cookies.REMEMBER_USERTYPE, vendorUser.getType().toString());
		sessionService.refreshCurrentSession(p, true);
	}

	@Transactional
	@Override
	public VendorUser updatePassword(long id, String password) {
		VendorUser p = vendorUserRepo.get(id);
		p.setPassword1(Codecs.md5Hex(password.getBytes()));
		if (p.getStatus() == VendorUserStatus.INIT) {
			p.setStatus(VendorUserStatus.ENABLED);
		}
		return vendorUserRepo.save(p);
	}

}
