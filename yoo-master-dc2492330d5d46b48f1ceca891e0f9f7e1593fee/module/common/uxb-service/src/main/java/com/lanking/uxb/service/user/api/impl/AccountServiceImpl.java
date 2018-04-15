package com.lanking.uxb.service.user.api.impl;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.api.UserTypeService;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
@Service("accountService")
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService, ApplicationContextAware {

	private ApplicationContext appContext;

	private Map<UserType, AccountService> accountServices = Maps.newHashMap();

	private AccountService defAccountService;

	@Autowired
	private SessionService sessionService;
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public Account getAccount(long accountId) {
		return defAccountService().getAccount(accountId);
	}

	@Override
	public Account getAccountByUserId(long userId) {
		return defAccountService().getAccountByUserId(userId);
	}

	@Override
	public Map<Long, Account> getAccountByUserIds(Set<Long> userIds) {
		return defAccountService().getAccountByUserIds(userIds);
	}

	@Override
	public Account getAccount(GetType getType, String value) {
		return defAccountService().getAccount(getType, value);
	}

	@Override
	public int getMaxAccountNameNum(String accountName) {
		return defAccountService().getMaxAccountNameNum(accountName);
	}

	@Transactional
	@Override
	public Account updateAccountStatus(String email, Status status) {
		return defAccountService().updateAccountStatus(email, status);
	}

	@Transactional
	@Override
	public void updateActivationStatus(long id, Status activationStatus) {
		defAccountService().updateActivationStatus(id, activationStatus);
	}

	@Transactional
	@Override
	public Account updateAccountMobileStatus(String mobile, Status status, Long userId) {
		return defAccountService().updateAccountMobileStatus(mobile, status, userId);
	}

	@Transactional
	@Override
	public Account updateAccountEmailStatus(String email, Status status, Long userId) {
		return defAccountService().updateAccountEmailStatus(email, status, userId);
	}

	@Transactional
	@Override
	public Account createAccount(RegisterForm form) throws AccountException {
		if (form.getType() == null) {
			throw new IllegalArgException();
		}
		return getAccountService(form.getType()).createAccount(form);
	}

	@Transactional
	@Override
	public Account createAccount2(RegisterForm form, boolean hasPassword) throws AccountException {
		if (form.getType() == null) {
			throw new IllegalArgException();
		}
		return getAccountService(form.getType()).createAccount2(form, hasPassword);
	}

	@Override
	public User getUserByAccountId(long accountId) {
		return defAccountService().getUserByAccountId(accountId);
	}

	@Override
	public User getUserByUserId(long userId) {
		return defAccountService().getUserByUserId(userId);
	}

	@Transactional
	@Override
	public Account updatePassword(String email, String password) {
		return defAccountService().updatePassword(email, password);
	}

	@Transactional
	@Override
	public Account updatePassword(long accountId, String password) {
		return defAccountService().updatePassword(accountId, password);
	}

	@Transactional
	@Override
	public Account updatePassword(Long accountId, String password, Integer strength) {
		return defAccountService.updatePassword(accountId, password, strength);
	}

	@Override
	@Transactional
	public Account updatePasswordStatus(long accountId, PasswordStatus passwordStatus) {
		return defAccountService.updatePasswordStatus(accountId, passwordStatus);
	}

	@Transactional
	@Override
	public Account updateMobileByUserId(long userId, String mobile) {
		return defAccountService().updateMobileByUserId(userId, mobile);
	}

	@Transactional
	@Override
	public Account updateNameByUserId(long userId, String name) {
		return defAccountService().updateNameByUserId(userId, name);
	}

	@Transactional
	@Override
	public Account updatePasswordMobile(long accountId, String mobile) {
		return defAccountService().updatePasswordMobile(accountId, mobile);
	}

	@Transactional
	@Override
	public Account updateName(long accountId, String name) {
		return defAccountService().updateName(accountId, name);
	}

	@Override
	public String generateName(UserType userType, GetType registerType) {
		return defAccountService().generateName(userType, registerType);
	}

	@Transactional
	@Override
	public void delete(long accountId) {
		defAccountService().delete(accountId);
	}

	@Transactional
	@Override
	public void handleLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		getAccountService(user.getUserType()).handleLogin(user, request, response);
		// 首次登陆成功后更新激活状态
		updateActivationStatus(user.getAccountId(), Status.ENABLED);
		SessionPacket p = new SessionPacket();
		p.setUserId(user.getId());
		p.setAccountId(user.getAccountId());
		p.setLoginSource(user.getLoginSource());
		// 记录用户类型到cookie中
		WebUtils.addCookie(request, response, Cookies.REMEMBER_USERTYPE, user.getUserType().toString());
		sessionService.refreshCurrentSession(p, true);
	}

	public void afterPropertiesSet() {
		for (AccountService accountService : appContext.getBeansOfType(AccountService.class).values()) {
			if (accountService instanceof UserTypeService) {
				if (defAccountService == null) {
					defAccountService = accountService;
				}
				accountServices.put(((UserTypeService) accountService).getType(), accountService);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
		afterPropertiesSet();
	}

	private void init() {
		if (defAccountService == null) {
			afterPropertiesSet();
		}
	}

	private AccountService defAccountService() {
		init();
		return defAccountService;
	}

	private AccountService getAccountService(UserType type) {
		init();
		AccountService accountService = null;
		if (type == null) {
			accountService = accountServices.get(UserType.PARENT);
		} else {
			accountService = accountServices.get(type);
		}
		return accountService;
	}

}
