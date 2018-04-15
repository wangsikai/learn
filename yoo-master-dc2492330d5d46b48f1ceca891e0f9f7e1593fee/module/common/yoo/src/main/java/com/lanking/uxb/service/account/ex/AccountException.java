package com.lanking.uxb.service.account.ex;

import com.lanking.cloud.ex.base.AbstractBasicServiceException;

/**
 * 账户相关异常
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public class AccountException extends AbstractBasicServiceException {

	private static final long serialVersionUID = 4014574573257793165L;

	static final int ACCOUNT_ERROR = 400;
	/**
	 * 用户名不能为空
	 */
	public static final int ACCOUNT_NAME_NULL = ACCOUNT_ERROR + 1;
	/**
	 * 密码不能为空
	 */
	public static final int ACCOUNT_PASSWORD_NULL = ACCOUNT_ERROR + 2;
	/**
	 * 密码不一致，请再次确认
	 */
	public static final int ACCOUNT_2PWD_NOT_CONSISTENT = ACCOUNT_ERROR + 3;
	/**
	 * 邮箱不能为空
	 */
	public static final int ACCOUNT_EMAIL_NULL = ACCOUNT_ERROR + 4;
	/**
	 * 手机不能为空
	 */
	public static final int ACCOUNT_MOBILE_NULL = ACCOUNT_ERROR + 5;
	/**
	 * 该用户名已被注册，请使用其他用户名注册。
	 */
	public static final int ACCOUNT_NAME_EXIST = ACCOUNT_ERROR + 6;
	/**
	 * 此邮箱已经存在，请检查拼写。
	 */
	public static final int ACCOUNT_EMAIL_EXIST = ACCOUNT_ERROR + 7;
	/**
	 * 手机存在
	 */
	public static final int ACCOUNT_MOBILE_EXIST = ACCOUNT_ERROR + 8;
	/**
	 * 用户名格式不正确，请确认只含有汉字、数字、字母和‘_’
	 */
	public static final int ACCOUNT_NAME_INVALID = ACCOUNT_ERROR + 9;
	/**
	 * 邮箱格式不正确
	 */
	public static final int ACCOUNT_EMAIL_INVALID = ACCOUNT_ERROR + 10;
	/**
	 * 手机格式不正确
	 */
	public static final int ACCOUNT_MOBILE_INVALID = ACCOUNT_ERROR + 11;
	/**
	 * 密码格式不正确
	 */
	public static final int ACCOUNT_PASSWORD_INVALID = ACCOUNT_ERROR + 12;
	/**
	 * 激活链接无效
	 */
	public static final int ACCOUNT_ACTIVE_URL_INVALID = ACCOUNT_ERROR + 13;
	/**
	 * 用户名或邮箱不存在
	 */
	public static final int ACCOUNT_NAME_EMAIL_NOT_EXIST = ACCOUNT_ERROR + 14;
	/**
	 * 密码错误
	 */
	public static final int ACCOUNT_PASSWORD_WRONG = ACCOUNT_ERROR + 15;
	/**
	 * 账号未激活
	 */
	public static final int ACCOUNT_NOT_ACTIVE = ACCOUNT_ERROR + 16;
	/**
	 * 账号已被删除
	 */
	public static final int ACCOUNT_DELETED = ACCOUNT_ERROR + 17;
	/**
	 * 验证码错误
	 */
	public static final int ACCOUNT_VERIFYCODE_INVALID = ACCOUNT_ERROR + 18;
	/**
	 * 需要完善资料
	 */
	public static final int ACCOUNT_FILL_PROFILE = ACCOUNT_ERROR + 19;
	/**
	 * 找回密码链接无效
	 */
	public static final int ACCOUNT_URL_INVALID = ACCOUNT_ERROR + 20;
	/**
	 * 原密码错误
	 */
	public static final int ACCOUNT_OLDPWD_WRONG = ACCOUNT_ERROR + 21;
	/**
	 * 已经登录,无需登录
	 */
	public static final int ACCOUNT_NOT_NEED_LOGIN = ACCOUNT_ERROR + 22;
	/**
	 * 需要验证码
	 */
	public static final int ACCOUNT_NEET_VERIFYCODE = ACCOUNT_ERROR + 23;
	/**
	 * 班级码不存在
	 */
	public static final int ACCOUNT_CLASSCODE_NOT_EXIST = ACCOUNT_ERROR + 24;
	/**
	 * 需要设置密码
	 */
	public static final int ACCOUNT_NEED_RESET_PASSWORD = ACCOUNT_ERROR + 25;
	/**
	 * 登录不支持的用户类型
	 */
	public static final int LOGIN_NOT_SUPPORT_USERTYPE = ACCOUNT_ERROR + 26;
	/**
	 * 验证码超时
	 */
	public static final int ACCOUNT_VERIFYCODE_TIMEOUT = ACCOUNT_ERROR + 27;

	/**
	 * 验证码还未过期,防止用户刷新再发送至同一号码
	 */
	public static final int ACCOUNT_VERIFYCODE_NOT_EXPIRE = ACCOUNT_ERROR + 28;
	/**
	 * 账号被禁用
	 */
	public static final int ACCOUNT_FORBIDDEN = ACCOUNT_ERROR + 29;
	/**
	 * 账号长时间未登录
	 */
	public static final int ACCOUNT_LONG_NOLOGIN = ACCOUNT_ERROR + 30;
	/**
	 * 账号异常
	 */
	public static final int ACCOUNT_EXCEPTION = ACCOUNT_ERROR + 31;

	/**
	 * 没有密码不能删除第三方凭证
	 */
	public static final int ACCOUNT_NOPWD_CANNOT_DELETECREDENTIAL = ACCOUNT_ERROR + 32;

	public AccountException() {
		super();
	}

	public AccountException(int code, Object... args) {
		super(code, args);
	}

	public AccountException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public AccountException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}

}
