package com.lanking.cloud.ex.code.core;

import com.lanking.cloud.ex.code.StatusCode;

/**
 * 核心异常代码
 * 
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public interface CoreExceptionCode extends StatusCode {

	/**
	 * 内部异常
	 */
	static final int SERVER_EX = 1;

	/**
	 * 空指异常
	 */
	static final int NPE_EX = 2;

	/**
	 * IO异常
	 */
	static final int IO_EX = 3;

	/**
	 * 数据库访问异常
	 */
	static final int DB_EX = 4;

	/**
	 * 缓存异常
	 */
	static final int CACHE_EX = 5;

	/**
	 * 不支持此操作异常
	 */
	static final int UN_SUPPORTED_OPERATION_EX = 6;

	/**
	 * 无权限异常
	 */
	static final int NO_PERMISSION_EX = 7;

	/**
	 * 非法参数异常
	 */
	static final int ILLEGAL_ARG_EX = 8;

	/**
	 * 非法参数格式异常
	 */
	static final int ILLEGAL_ARG_FORMAT_EX = 9;

	/**
	 * 参数不完整异常
	 */
	static final int MISSING_ARG_EX = 10;

	/**
	 * 实体对象异常
	 */
	static final int ENTITY_EX = 11;

	/**
	 * 实体对象未找到异常
	 */
	static final int ENTITY_NOT_FOUND_EX = 12;

	/**
	 * 实体对象已存在异常
	 */
	static final int ENTITY_EXISTS_EX = 13;

	/**
	 * 校验异常
	 */
	static final int VALIDATION_EX = 14;

	/**
	 * 未实现异常
	 */
	static final int NOT_IMPL_EX = 15;

	/**
	 * BIZ业务码不存在异常
	 */
	static final int BIZ_NOT_FOUND_EX = 16;

	/**
	 * 404异常
	 */
	static final int PAGE_NOT_FOUND_EX = 17;

	/**
	 * api访问频率限制异常
	 */
	static final int API_ACCESS_RATE_LIMIT_EX = 18;

	/**
	 * 不支持的profile异常
	 */
	static final int PROFILE_NOT_SUPPORT_EX = 19;

	/**
	 * 会员权限异常
	 */
	static final int MEMBER_PRIVILEGES_EX = 20;
}
