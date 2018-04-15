package com.lanking.uxb.ycoorect.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserResponse;
import com.lanking.intercomm.yoocorrect.service.CorrectUserDatawayService;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.ycoorect.ex.YooCorrectException;

/**
 * 登录接口.
 * 
 * @author wanlong.che
 *
 */
@RestController
@RequestMapping(value = "ycorrect/user")
public class YooCorrectLoginController {

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CorrectUserDatawayService correctUserDatawayService;

	/**
	 * 页面登录.
	 * 
	 * @param username
	 *            账户名
	 * @param password
	 *            密码
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "login")
	public Value login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return new Value(new MissingArgumentException());
		}

		Account account = null;
		if (username.contains("@")) {
			try {
				ValidateUtils.validateEmail(username);
			} catch (AccountException e) {
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
					return new Value(e);
				}
				account = accountService.getAccount(GetType.NAME, username);
			}
		}

		if (account == null) {
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}
		if (account.getStatus() == Status.DISABLED) {
			return new Value(new AccountException(AccountException.ACCOUNT_NOT_ACTIVE), account.getEmail());
		}
		if (account.getStatus() == Status.DELETED) {
			return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
		}

		if (!account.getPassword().equals(Codecs.md5Hex(password.getBytes()))) {
			return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
		}

		try {
			User user = accountService.getUserByAccountId(account.getId());
			if (user.getStatus() == Status.DISABLED) {
				return new Value(new AccountException(AccountException.ACCOUNT_FORBIDDEN));
			}
			if (user.getStatus() == Status.DELETED) {
				return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
			}
			if (user.getUserType() != UserType.TEACHER) {
				// 需要教师账户登录
				return new Value(new AccountException(YooCorrectException.NEED_TEACHER_LOGIN));
			}

			user.setLoginSource(Product.YOOMATH);
			accountService.handleLogin(user, request, response);

			// 获取小优快批用户
			CorrectUserResponse correctUser = correctUserDatawayService.getCorrectUserByUxbUserId(user.getId(), true);

			// 缓存当前用户对应的快批用户的ID
			if (correctUser != null) {
				SessionPacket sessionPacket = sessionService.getSessionPacket(Security.getToken());
				sessionPacket.getAttrSession().setAttr("correctUser", correctUser);
				sessionService.refreshCurrentSession(sessionPacket, false);
				SecurityContext.setCorrectUserId(correctUser.getId());
			}

			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "0");
			WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, "0");
			WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");

			return new Value();
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 页面退出接口.
	 * 
	 * @return
	 */
	@RequestMapping(value = "logout")
	public Value logout(HttpServletRequest request, HttpServletResponse response) {
		if (Security.isLogin()) {
			sessionService.offline(request, response);
		}
		WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERTYPE);
		return new Value();
	}
}
