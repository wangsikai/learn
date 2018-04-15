package com.lanking.uxb.rescon.account.controller;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.support.resources.vendor.Vendor;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorAuthManage;
import com.lanking.uxb.rescon.account.api.ResconVendorManage;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.account.cache.ResconAccountCacheService;
import com.lanking.uxb.rescon.account.convert.ResconVendorUserConvert;
import com.lanking.uxb.rescon.account.form.ResconUserForm;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.rescon.account.value.VVendorUserCount;
import com.lanking.uxb.rescon.auth.api.DataPermission;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 帐号管理.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年8月12日
 */
@RestController
@RequestMapping("rescon/account")
public class ResconUserAccountController {

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconVendorUserConvert vendorUserConvert;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ResconAccountCacheService accountCacheService;
	@Autowired
	private ResconVendorAuthManage vendorAuthManage;
	@Autowired
	private ResconVendorManage vendorManage;

	/**
	 * 创建/修改账号
	 * 
	 * @param resconUserForm
	 * @return
	 */
	@RequestMapping(value = "create", method = { RequestMethod.POST, RequestMethod.GET })
	public Value create(ResconUserForm resconUserForm, DataPermission permission) {
		VendorUser admin = vendorUserManage.getVendorUser(Security.getUserId());
		VendorUser user = new VendorUser();
		user.setRealName(resconUserForm.getRealName());
		user.setPassword2(resconUserForm.getPassword());
		user.setPassword1(Codecs.md5Hex(user.getPassword2().getBytes()));
		user.setStatus(resconUserForm.getStatus());
		user.setType(resconUserForm.getUserType());
		user.setVendorId(admin.getVendorId());
		if (null != permission) {
			user.setPermissions(JSON.toJSONString(permission));
		}
		if (null != resconUserForm.getId()) {
			user.setId(resconUserForm.getId());
		}
		return new Value(vendorUserConvert.to(vendorUserManage.create(user)));
	}

	/**
	 * 查询账号列表
	 * 
	 * @param userType
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "12") int pageSize,
			@RequestParam(value = "userType", required = false) UserType userType,
			@RequestParam(value = "status", required = false) VendorUserStatus status) {
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		Page<VendorUser> cp = vendorUserManage.getVendorUserList(
				P.index(page, pageSize),
				vendorId,
				status == null ? Lists.newArrayList(VendorUserStatus.ENABLED, VendorUserStatus.DISABLED) : Lists
						.newArrayList(status), userType);
		VPage<VVendorUser> vp = new VPage<VVendorUser>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(vendorUserConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 获取供应商用户统计数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "getUserCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getUserCount() {
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		VVendorUserCount vVendorUserCount = new VVendorUserCount();
		vVendorUserCount.setTotal(vendorUserManage.getVendorUserCount(vendorId));
		vVendorUserCount
				.setEnableCount(vendorUserManage.getVendorUserCountByStatus(vendorId, VendorUserStatus.ENABLED));
		vVendorUserCount.setCheckCount(vendorUserManage.getVendorUserCountByUserType(vendorId, UserType.VENDOR_CHECK));
		vVendorUserCount.setBuildCount(vendorUserManage.getVendorUserCountByUserType(vendorId, UserType.VENDOR_BUILD));
		vVendorUserCount.setHeadCount(vendorUserManage.getVendorUserCountByUserType(vendorId, UserType.VENDOR_HEAD));
		return new Value(vVendorUserCount);
	}

	/**
	 * 获取用户对应的信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getVendorUser", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getVendorUser(@RequestParam(value = "id", required = false) Long id) {
		if (id == null) {
			id = Security.getUserId();
		}
		return new Value(vendorUserConvert.to(vendorUserManage.getVendorUser(id)));
	}

	/**
	 * 登录.
	 * 
	 * @param username
	 * @param password
	 * @param verifyCode
	 * @param remember
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
	public Value login(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "verifyCode", required = false) String verifyCode,
			@RequestParam(value = "remember", required = false, defaultValue = "") String remember,
			HttpServletRequest request, HttpServletResponse response) {
		if (Security.isLogin()) {
			return new Value(new AccountException(AccountException.ACCOUNT_NOT_NEED_LOGIN));
		}
		long wrongTime = accountCacheService.getLoginWrongTime(Security.getToken());
		if (wrongTime > 0 && (!Security.checkVerifyCode(verifyCode))) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}
		VendorUser user = vendorUserManage.findOne(GetType.NAME, username);
		if (user == null) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}
		if (user.getStatus() == VendorUserStatus.DISABLED || user.getStatus() == VendorUserStatus.DELETED) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
		}
		if (!user.getPassword1().equals(Codecs.md5Hex(password.getBytes()))) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "1");
			return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
		}
		if (user.getStatus() == VendorUserStatus.INIT) {
			return new Value(new AccountException(AccountException.ACCOUNT_NEED_RESET_PASSWORD));
		}

		Vendor vendor = vendorManage.get(user.getVendorId());
		if (!vendor.getResFlag()) {
			return new Value(new NoPermissionException()); // 无权限登录
		}
		try {
			vendorAuthManage.handleLogin(user, request, response);

			accountCacheService.invalidLoginWrongTime(Security.getToken());
			if ("1".equals(remember)) {
				WebUtils.addCookie(request, response, Cookies.REMEMBER_USERNAME, URLEncoder.encode(username),
						(int) TimeUnit.DAYS.toSeconds(30L));
			} else {
				WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERNAME);
			}
			WebUtils.addCookie(request, response, Cookies.LOGIN_NEET_VERIFYCODE, "0");
			return new Value();
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 登出.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "logout", "offline" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value logout(HttpServletRequest request, HttpServletResponse response) {
		if (Security.isLogin()) {
			sessionService.offline(request, response);
		}
		WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERTYPE);
		return new Value();
	}

	@RequestMapping(value = { "sync" }, method = { RequestMethod.POST, RequestMethod.GET })
	@RolesAllowed(anyone = true)
	public Value sync() {
		return new Value(Security.getUserType() == null ? "" : Security.getUserType().toString());
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "init" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value initAccount(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, @RequestParam(value = "password1") String password1,
			@RequestParam(value = "password2") String password2, HttpServletRequest request,
			HttpServletResponse response) {
		VendorUser user = vendorUserManage.findOne(GetType.NAME, username);
		if (user == null) {
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}
		if (user.getStatus() == VendorUserStatus.INIT) {
			if (user.getStatus() == VendorUserStatus.DISABLED || user.getStatus() == VendorUserStatus.DELETED) {
				return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
			}
			if (!user.getPassword1().equals(Codecs.md5Hex(password.getBytes()))) {
				return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
			}

			try {
				ValidateUtils.validatePassword(password1);
				if (!password1.equals(password2)) {
					throw new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT);
				}
			} catch (AccountException e) {
				return new Value(e);
			}
			vendorAuthManage.updatePassword(user.getId(), password1);
			return new Value();
		} else {
			return new Value(new NoPermissionException());
		}
	}
}
