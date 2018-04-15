package com.lanking.uxb.service.adminSecurity.controller;

import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleAuthManage;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.cache.ConsoleUserCacheService;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleUserConvert;
import com.lanking.uxb.service.adminSecurity.ex.AdminSecurityException;
import com.lanking.uxb.service.adminSecurity.form.ConsoleUserForm;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.adminSecurity.value.VConsoleUser;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "con/user")
public class ConsoleUserController {
	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private ConsoleUserCacheService consoleUserCacheService;
	@Autowired
	private ConsoleAuthManage consoleAuthManage;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ConsoleUserConvert consoleUserConvert;
	@Autowired
	private ConsoleSystemService consoleSystemService;

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(String saveForm) {
		if (StringUtils.isBlank(saveForm)) {
			return new Value(new IllegalArgException("saveForm"));
		}
		ConsoleUserForm form = JSONObject.parseObject(saveForm, ConsoleUserForm.class);
		// 不允许添加超级管理员！
		if (form.getSystemId().equals(0L)) {
			return new Value(new NoPermissionException());
		}
		/*
		 * ConsoleUser consoleUser =
		 * consoleUserService.get(Security.getUserId()); if
		 * (!consoleUser.getSystemId().equals(0L)) { return new Value(new
		 * LddpException(LddpException.NO_PERMISSON)); }
		 */

		consoleUserService.save(form);

		return new Value();
	}

	/**
	 * 分页查询用户
	 *
	 * @param systemId
	 *            系统id
	 * @param name
	 *            名称
	 * @param size
	 *            大小
	 * @param page
	 *            当前页
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(Long systemId, String name, @RequestParam(value = "size", defaultValue = "50") int size,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		/*
		 * ConsoleUser consoleUser =
		 * consoleUserService.get(Security.getUserId());
		 * 
		 * if (!consoleUser.getSystemId().equals(0L)) { return new Value(new
		 * LddpException(LddpException.NO_PERMISSON)); }
		 */

		Pageable pageable = P.index(page, size);
		Page<ConsoleUser> users = consoleUserService.query(systemId, name, pageable);
		List<VConsoleUser> vs = consoleUserConvert.to(users.getItems());
		VPage<VConsoleUser> vpage = new VPage<VConsoleUser>();
		vpage.setCurrentPage(page);
		vpage.setPageSize(size);
		vpage.setTotal(users.getTotalCount());
		vpage.setTotalPage(users.getPageCount());
		vpage.setItems(vs);

		return new Value(vpage);
	}

	@ConsoleRolesAllowed(anyone = true)
	@RequestMapping(value = { "logout", "offline" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value logout(HttpServletRequest request, HttpServletResponse response) {
		if (Security.isLogin()) {
			sessionService.offline(request, response);
		}
		WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERTYPE);
		return new Value();
	}

	@ConsoleRolesAllowed(anyone = true)
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "login", method = { RequestMethod.GET, RequestMethod.POST })
	public Value login(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "verifyCode", required = false) String verifyCode,
			@RequestParam(value = "remember", required = false, defaultValue = "") String remember,
			@RequestParam(value = "systemId") Long systemId, HttpServletRequest request, HttpServletResponse response) {

		ConsoleSystem consoleSystem = consoleSystemService.get(systemId);
		if (consoleSystem.getStatus() != Status.ENABLED) {
			return new Value(new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_DELETED));
		}

		if (Security.isLogin()) {
			new Value(new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_NOT_NEED_LOGIN));
		}
		long wrongTime = consoleUserCacheService.getLoginWrongTime(Security.getToken());
		if (wrongTime > 0 && (!Security.checkVerifyCode(verifyCode))) {
			consoleUserCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_VERIFYCODE_INVALID));
		}
		ConsoleUser user = consoleUserService.getByName(userName, systemId);
		if (user == null) {
			consoleUserCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_NAME_EMAIL_NOT_EXIST));
		}
		if (user.getStatus() == Status.DISABLED || user.getStatus() == Status.DELETED) {
			consoleUserCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_DELETED));
		}
		if (!user.getPassword().equals(Codecs.md5Hex(password))) {
			consoleUserCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_PASSWORD_WRONG));
		}

		try {
			consoleAuthManage.handleLogin(user, request, response);

			consoleUserCacheService.incrLoginWrongTime(Security.getToken());
			if ("1".equals(remember)) {
				WebUtils.addCookie(request, response, Cookies.REMEMBER_USERNAME, URLEncoder.encode(userName),
						(int) TimeUnit.DAYS.toSeconds(30L));
			} else {
				WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERNAME);
			}
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "0");
			WebUtils.addCookie(request, response,
					com.lanking.cloud.domain.base.session.api.Cookies.SECURITY_LOGIN_STATUS, "1");
			consoleUserCacheService.invalidLoginWrongTime(Security.getToken());

			return new Value();
		} catch (AdminSecurityException e) {
			return new Value(e);
		}

	}

	@RequestMapping(value = "modify_password", method = { RequestMethod.GET, RequestMethod.POST })
	public Value modifyPassword(@RequestParam(value = "oldPassword") String oldPassword,
			@RequestParam(value = "newPassword") String newPassword) {
		ConsoleUser consoleUser = consoleUserService.get(Security.getUserId());
		// 原密码与不正确
		if (!consoleUser.getPassword().equals(Codecs.md5Hex(oldPassword.getBytes()))) {
			return new Value(new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_PASSWORD_ERROR));
		}
		ConsoleUserForm form = new ConsoleUserForm();
		form.setId(consoleUser.getId());
		form.setPlainPassword(newPassword);

		consoleUserService.update(form);
		return new Value();
	}

	/**
	 * 更新用户的状态，禁用或者启用
	 *
	 * @param userId
	 *            用户id
	 * @param status
	 *            {@link Status}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "updateUserStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateUserStatus(long userId, Status status) {
		try {
			consoleUserService.updateUserStatus(userId, status);
		} catch (AdminSecurityException e) {
			return new Value(e);
		}
		return new Value();
	}

}
