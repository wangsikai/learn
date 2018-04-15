package com.lanking.uxb.service.user.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月11日
 *
 */
@Service("pAccountService")
@Transactional(readOnly = true)
public class ParentAccountServiceImpl extends AbstractAccountService {

	@Override
	public UserType getType() {
		return UserType.PARENT;
	}

	@Override
	public boolean accept(UserType userType) {
		return userType == UserType.PARENT;
	}

	@Override
	public UserType getLoginUserType() {
		return UserType.PARENT;
	}

	@Override
	public void handleLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		handleLogin();
	}

	@Transactional
	@Override
	public Account createAccount(RegisterForm form) throws AccountException {
		super.createAccount(form);
		return null;
	}

	@Transactional
	@Override
	public Account createAccount2(RegisterForm form, boolean hasPassword) throws AccountException {
		super.createAccount2(form, hasPassword);
		return null;
	}
}
