package com.lanking.uxb.service.web.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.service.user.resource.PasswordController;

/**
 * 作业密码相关接口
 * 
 * @since yoomath V1.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月11日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/account/pwd")
public class ZyPasswordController {
	@Autowired
	private PasswordController passwordController;

	/**
	 * 通过原密码修改密码
	 * 
	 * @param oldPwd
	 *            原密码
	 * @param password
	 *            新密码
	 * @param pwd
	 *            新密码
	 * @param strength
	 *            密码强度
	 * @param request
	 * @param response
	 * @return {@link Value}
	 */

	@RequestMapping(value = { "reset_pwd" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value resetPwdByPwd(@RequestParam(value = "oldPwd", required = false) String oldPwd,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "pwd", required = false) String pwd,
			@RequestParam(value = "strength", required = false, defaultValue = "2") Integer strength,
			HttpServletRequest request, HttpServletResponse response) {
		return passwordController.resetPwdByPwd(oldPwd, password, pwd, strength, false, request, response);
	}
}
