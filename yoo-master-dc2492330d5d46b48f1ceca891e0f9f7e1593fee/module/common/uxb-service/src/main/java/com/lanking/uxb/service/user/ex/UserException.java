package com.lanking.uxb.service.user.ex;

import com.lanking.cloud.ex.base.AbstractBasicServiceException;

/**
 * 用户相关异常
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public class UserException extends AbstractBasicServiceException {

	private static final long serialVersionUID = 5864773369192099517L;

	static final int USER_ERROR = 450;

	/**
	 * 已经是好友
	 */
	public static final int USER_IS_FRIEND = USER_ERROR + 1;
	/**
	 * 不能添加自己为好友
	 */
	public static final int USER_FRIEND_CANNOT_SELF = USER_ERROR + 2;
	/**
	 * 学校码无效
	 */
	public static final int USER_SCHOOLCERT_CODE_INVALID = USER_ERROR + 3;
	/**
	 * 系统没有查到您的手机号码匹配信息
	 */
	public static final int USER_SCHOOLCERT_MOBILE_INVALID = USER_ERROR + 4;
	/**
	 * 您已经是学校认证用户
	 */
	public static final int USER_SCHOOL_CERTED = USER_ERROR + 5;
	/**
	 * 请进行学校码认证
	 */
	public static final int USER_SCHOOLCODE_NOT_CERTED = USER_ERROR + 6;
	/**
	 * 学校认证手机验证码无效
	 */
	public static final int USER_SCHOOLCERT_SMSCODE_INVALID = USER_ERROR + 7;
	/**
	 * 不能关注自己
	 */
	public static final int USER_CANNOT_FOLLOW_SELF = USER_ERROR + 8;
	/**
	 * 学生基础信息核对失败
	 */
	public static final int USER_STUDENT_BASE_CHECK_FAIL = USER_ERROR + 9;
	/**
	 * 此学生已经被认证
	 */
	public static final int USER_STUDENT_BASE_CERTED = USER_ERROR + 10;
	/**
	 * 需要完善资料
	 */
	public static final int USER_FILL_PROFILE = USER_ERROR + 11;
	/**
	 * 需要认证学校
	 */
	public static final int USER_CERT_SCHOOL = USER_ERROR + 12;
	/**
	 * 超过用户最大关注数
	 */
	public static final int USER_MAX_FOLLOW = USER_ERROR + 13;
	/**
	 * 没有足够积分
	 */
	public static final int NOT_ENOUGH_POINT = USER_ERROR + 14;

	public UserException() {
		super();
	}

	public UserException(int code, Object... args) {
		super(code, args);
	}

	public UserException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public UserException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}
}
