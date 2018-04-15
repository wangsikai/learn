package com.lanking.uxb.service.adminSecurity.ex;

import com.lanking.cloud.ex.support.AbstractSupportSystemCommonException;

/**
 * 管理系统账户/用户/权限异常
 *
 * 自3.0.2版本以后此Exception错误码从1000001~1001000
 * 
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年10月27日
 */
public class AdminSecurityException extends AbstractSupportSystemCommonException {

	private static final long serialVersionUID = -4796567098344574449L;

	static final int ADMIN_SECURITY_ERROR = 1000000;

	/**
	 * 　密码为空
	 */
	public static final int ADMIN_SECURITY_PLAIN_PASSWORD_ERROR = ADMIN_SECURITY_ERROR + 1;

	/**
	 * 修改之前原密码不正确
	 */
	public static final int ADMIN_SECURITY_PASSWORD_ERROR = ADMIN_SECURITY_ERROR + 2;

	/**
	 * 用户不存在
	 */
	public static final int ADMIN_SECURITY_NOT_EXISTS = ADMIN_SECURITY_ERROR + 3;
	/**
	 * 用户已经被删除
	 */
	public static final int ADMIN_SECURITY_DELETED = ADMIN_SECURITY_ERROR + 4;
	/**
	 * 用户已经登录，不需要再登录
	 */
	public static final int ADMIN_SECURITY_NOT_NEED_LOGIN = ADMIN_SECURITY_ERROR + 5;
	/**
	 * 验证码错误
	 */
	public static final int ADMIN_SECURITY_VERIFYCODE_INVALID = ADMIN_SECURITY_ERROR + 6;
	/**
	 * 用户名或邮箱不存在
	 */
	public static final int ADMIN_SECURITY_NAME_EMAIL_NOT_EXIST = ADMIN_SECURITY_ERROR + 7;
	/**
	 * 用户错误密码
	 */
	public static final int ADMIN_SECURITY_PASSWORD_WRONG = ADMIN_SECURITY_ERROR + 8;

	public AdminSecurityException(int code, Object... args) {
		super(code, args);
	}

}
