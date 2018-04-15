package com.lanking.uxb.service.user.resource;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.CommonController;
import com.lanking.uxb.service.thirdparty.api.ThirdpartyService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.convert.AccountConvert;
import com.lanking.uxb.service.user.convert.UserConvert;

/**
 * 账号相关接口
 * 
 * @version 2.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
@RestController
@RequestMapping("account")
public class AccountController {

	@Autowired
	private SessionService sessionService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private AccountConvert accountConvert;
	@Autowired
	private ThirdpartyService thirdpartyService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	@Qualifier("thirdpartyCommonController")
	private CommonController commonController;

	@SuppressWarnings("deprecation")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
	public Value login(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "verifyCode", required = false) String verifyCode,
			@RequestParam(value = "remember", required = false, defaultValue = "0") String remember,
			@RequestParam(value = "source") Product source, HttpServletRequest request, HttpServletResponse response) {
		long wrongTime = accountCacheService.getLoginWrongTime(Security.getToken());
		long wrongPwdTime = accountCacheService.getLoginWrongTime(Security.getToken() + "_" + username);
		if (source == null) {// source为空时直接报错
			return new Value(new ServerException());
		}
		Account account = null;
		if (username.contains("@")) {
			try {
				ValidateUtils.validateEmail(username);
			} catch (AccountException e) {
				wrongTime = accountCacheService.incrLoginWrongTime(Security.getToken());
				return new Value(e);
			}
			account = accountService.getAccount(GetType.EMAIL, username);
		} else {
			try {
				ValidateUtils.validateMobile(username);
				account = accountService.getAccount(GetType.MOBILE, username);
			} catch (AccountException ex) {
				try {
					ValidateUtils.validateName(username);
				} catch (AccountException e) {
					wrongTime = accountCacheService.incrLoginWrongTime(Security.getToken());
					return new Value(e);
				}
				account = accountService.getAccount(GetType.NAME, username);
			}
		}
		if (account == null) {
			wrongTime = accountCacheService.incrLoginWrongTime(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}
		if (account.getStatus() == Status.DISABLED) {
			wrongTime = accountCacheService.incrLoginWrongTime(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_NOT_ACTIVE), account.getEmail());
		}
		if (account.getStatus() == Status.DELETED) {
			wrongTime = accountCacheService.incrLoginWrongTime(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
		}
		String superPassword = Env.getDynamicString("account.password.super");
		if (StringUtils.isNotBlank(superPassword) && superPassword.equals(password)) {
			// 超级密码直接过
		} else {
			if (!account.getPassword().equals(Codecs.md5Hex(password.getBytes()))) {
				wrongTime = accountCacheService.incrLoginWrongTime(Security.getToken());
				wrongPwdTime = accountCacheService.incrLoginWrongTime(Security.getToken() + "_" + username);
				WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, String.valueOf(wrongPwdTime));
				return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
			}
		}
		try {
			User user = accountService.getUserByAccountId(account.getId());
			if (user.getStatus() == Status.DISABLED) {
				return new Value(new AccountException(AccountException.ACCOUNT_FORBIDDEN));
			}
			if (user.getStatus() == Status.DELETED) {
				return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
			}
			user.setLoginSource(source);
			accountService.handleLogin(user, request, response);

			accountCacheService.invalidLoginWrongTime(Security.getToken());
			accountCacheService.invalidLoginWrongTime(Security.getToken() + "_" + username);
			if ("1".equals(remember)) {
				WebUtils.addCookie(request, response, Cookies.REMEMBER_USERNAME, URLEncoder.encode(username),
						(int) TimeUnit.DAYS.toSeconds(30L));
			} else {
				WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERNAME);
			}
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "0");
			WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, "0");

			return new Value(userConvert.to(user));
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "logout", "offline" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value logout(HttpServletRequest request, HttpServletResponse response) {
		if (Security.isLogin()) {
			thirdpartyService.logout(); // 第三方退出
			sessionService.offline(request, response);
		}
		WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERTYPE);
		return new Value();
	}

	@RequestMapping(value = { "info" }, method = RequestMethod.POST)
	public Value info() {
		Account account = accountService.getAccountByUserId(Security.getUserId());
		return new Value(accountConvert.to(account));
	}

	/**
	 * @since yooshare v2.3.2
	 * @return
	 */

	@RequestMapping(value = { "info2" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value info2(Product product) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		Account account = accountService.getAccountByUserId(Security.getUserId());
		map.put("account", accountConvert.to(account));
		map.put("thirds", commonController.listUserThirdCredential(product, "").getRet());
		return new Value(map);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "sync" }, method = RequestMethod.POST)
	public Value sync() {
		return new Value(Security.getUserType() == null ? "" : Security.getUserType().toString());
	}

	/**
	 * 验证用户密码.
	 * 
	 * @param pw
	 *            密码
	 * @return
	 */

	@RequestMapping(value = { "validPassword" }, method = RequestMethod.POST)
	public Value validPassword(String pw) {
		if (StringUtils.isBlank(pw)) {
			return new Value(new MissingArgumentException());
		}
		Account account = accountService.getAccountByUserId(Security.getUserId());
		if (account.getPassword().equals(Codecs.md5Hex(pw.getBytes()))) {
			return new Value(true);
		} else {
			return new Value(false);
		}
	}
}
