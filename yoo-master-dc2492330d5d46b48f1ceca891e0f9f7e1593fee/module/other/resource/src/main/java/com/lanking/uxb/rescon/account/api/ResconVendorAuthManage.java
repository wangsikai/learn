package com.lanking.uxb.rescon.account.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.uxb.service.session.api.SessionUserService;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年3月3日
 */
public interface ResconVendorAuthManage extends SessionUserService {

	/**
	 * 登录
	 * 
	 * @param vendorUser
	 *            登录管理员
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 */
	void handleLogin(VendorUser vendorUser, HttpServletRequest request, HttpServletResponse response);

	VendorUser updatePassword(long id, String password);
}
