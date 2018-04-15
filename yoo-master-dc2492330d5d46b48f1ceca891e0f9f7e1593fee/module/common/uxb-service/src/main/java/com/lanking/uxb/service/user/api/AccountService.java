package com.lanking.uxb.service.user.api;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * 账户相关接口(包括登陆注册相关)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public interface AccountService {

	Account getAccount(long accountId);

	Account getAccountByUserId(long userId);

	Map<Long, Account> getAccountByUserIds(Set<Long> userIds);

	Account getAccount(GetType getType, String value);

	int getMaxAccountNameNum(String accountName);

	Account createAccount(RegisterForm form) throws AccountException;

	Account createAccount2(RegisterForm form, boolean hasPassword) throws AccountException;

	Account updateAccountStatus(String email, Status status);

	void updateActivationStatus(long id, Status activationStatus);

	Account updateAccountMobileStatus(String mobile, Status status, Long userId);

	Account updateAccountEmailStatus(String email, Status status, Long userId);

	Account updatePassword(String email, String password);

	Account updatePassword(long accountId, String password);

	Account updatePassword(Long accountId, String password, Integer strength);

	Account updatePasswordStatus(long accountId, PasswordStatus passwordStatus);

	Account updateMobileByUserId(long userId, String mobile);

	Account updateNameByUserId(long userId, String name);

	Account updatePasswordMobile(long accountId, String mobile);

	Account updateName(long accountId, String name);

	/**
	 * 生成用户名
	 * 
	 * @since 2.1.0
	 * @param userType
	 *            用户类型
	 * @param registerType
	 *            注册类型
	 * @return 系统自动生成注册账号
	 */
	String generateName(UserType userType, GetType registerType);

	User getUserByAccountId(long accountId);

	User getUserByUserId(long userId);

	void delete(long accountId);

	void handleLogin(User user, HttpServletRequest request, HttpServletResponse response);

}