package com.lanking.uxb.service.user.api.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月11日
 *
 */
@Service("tAccountService")
@Transactional(readOnly = true)
public class TeacherAccountServiceImpl extends AbstractAccountService {

	@Override
	public UserType getType() {
		return UserType.TEACHER;
	}

	@Override
	public boolean accept(UserType userType) {
		return userType == UserType.TEACHER;
	}

	@Override
	public UserType getLoginUserType() {
		return UserType.TEACHER;
	}

	@Override
	public void handleLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		handleLogin();
	}

	@Transactional
	@Override
	public Account createAccount(RegisterForm form) throws AccountException {
		super.createAccount(form);
		if (StringUtils.isBlank(form.getEmail())) {
			throw new AccountException(AccountException.ACCOUNT_EMAIL_NULL);
		}
		Account account1 = getAccount(GetType.EMAIL, form.getEmail());
		if (account1 != null) {
			throw new AccountException(AccountException.ACCOUNT_EMAIL_EXIST);
		}
		ValidateUtils.validateEmail(form.getEmail());
		Account account = new Account();
		account.setEmail(form.getEmail());
		account.setName(form.getName());
		account.setPassword(Codecs.md5Hex(String.valueOf(form.getPassword())));
		account.setRegisterAt(new Date());

		Account ac = accountRepo.save(account);
		userService.createUser(ac, form);

		return ac;
	}

	@Transactional
	@Override
	public Account createAccount2(RegisterForm form, boolean hasPassword) throws AccountException {
		super.createAccount2(form, hasPassword);
		Account account = new Account();
		account.setName(form.getName());
		if (StringUtils.isNotBlank(form.getMobile())) {
			account.setMobile(form.getMobile());
			account.setMobileStatus(Status.ENABLED);
		}
		if (StringUtils.isNotBlank(form.getEmail())) {
			account.setEmail(form.getEmail());
			account.setEmailStatus(Status.ENABLED);
		}
		account.setPassword(Codecs.md5Hex(String.valueOf(form.getPassword())));
		account.setStatus(Status.ENABLED);
		account.setRegisterAt(new Date());
		if (StringUtils.isNotBlank(form.getPassword())) {
			account.setStrength(passwordStrength(form.getPassword()));
		}
		if (!hasPassword) {
			account.setPasswordStatus(PasswordStatus.DISABLED);
		}

		Account ac = accountRepo.save(account);
		ac.setUser(userService.createUser2(ac, form));
		return ac;
	}

}
