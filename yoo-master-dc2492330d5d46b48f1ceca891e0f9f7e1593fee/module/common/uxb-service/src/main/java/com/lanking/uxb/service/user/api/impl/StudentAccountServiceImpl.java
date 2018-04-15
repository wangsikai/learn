package com.lanking.uxb.service.user.api.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月11日
 *
 */
@Service("sAccountService")
@Transactional(readOnly = true)
public class StudentAccountServiceImpl extends AbstractAccountService {

	@Override
	public UserType getType() {
		return UserType.STUDENT;
	}

	@Override
	public boolean accept(UserType userType) {
		return userType == UserType.STUDENT;
	}

	@Override
	public UserType getLoginUserType() {
		return UserType.STUDENT;
	}

	@Override
	public void handleLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		handleLogin();
	}

	@Transactional
	@Override
	public Account createAccount(RegisterForm form) throws AccountException {
		super.createAccount(form);
		Account account = new Account();
		// 学生成功后就是激活状态
		account.setStatus(Status.ENABLED);
		account.setName(form.getName());
		account.setPassword(Codecs.md5Hex(String.valueOf(form.getPassword())));
		account.setRegisterAt(new Date());

		Account ac = accountRepo.save(account);
		userService.createUser(ac, form);
		return ac;
	}

	/**
	 * @since 2.1 2015-08-24 学生注册不再需要打码赛克
	 */
	@Transactional
	@Override
	public Account createAccount2(RegisterForm form, boolean hasPassword) throws AccountException {
		super.createAccount2(form, hasPassword);
		Account account = new Account();
		// 学生成功后就是激活状态
		account.setStatus(Status.ENABLED);
		account.setName(form.getName());
		account.setPassword(Codecs.md5Hex(String.valueOf(form.getPassword())));
		account.setRegisterAt(new Date());
		if (!hasPassword) {
			account.setPasswordStatus(PasswordStatus.DISABLED);
		}
		if (form.getStrength() != null) {
			account.setStrength(form.getStrength());
		}
		if (StringUtils.isNotBlank(form.getMobile())) {
			account.setMobile(form.getMobile());
			account.setMobileStatus(Status.ENABLED);
		}
		Account ac = accountRepo.save(account);
		ac.setUser(userService.createUser2(ac, form));
		return ac;
	}
}
