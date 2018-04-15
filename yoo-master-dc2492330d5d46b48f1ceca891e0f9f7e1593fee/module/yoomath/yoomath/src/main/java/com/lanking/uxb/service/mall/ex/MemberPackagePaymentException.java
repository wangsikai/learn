package com.lanking.uxb.service.mall.ex;

import com.lanking.cloud.ex.AbstractException;

/**
 * 会员套餐支付相关异常.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月27日
 */
public class MemberPackagePaymentException extends AbstractException {
	private static final long serialVersionUID = -8885269910401941127L;

	private static final int MEMPACK_PAY_ERROR = 2850;

	/**
	 * 套餐已下架.
	 */
	public static final int MEMPACK_UNPUBLISH = MEMPACK_PAY_ERROR + 1;

	/**
	 * 已是校级会员，不能开通个人会员.
	 */
	public static final int MEMPACK_NOT_MATCH = MEMPACK_PAY_ERROR + 2;

	/**
	 * 当期套餐购买重复.
	 */
	public static final int MEMPACK_BUY_DUPLICATE = MEMPACK_PAY_ERROR + 3;

	/**
	 * 会员卡卡号不正确.
	 */
	public static final int MEMPACK_CARD_NOT_FOUND = MEMPACK_PAY_ERROR + 4;

	/**
	 * 会员卡被冻结.
	 */
	public static final int MEMPACK_CARD_DISABLE = MEMPACK_PAY_ERROR + 5;

	/**
	 * 会员卡已过期.
	 */
	public static final int MEMPACK_CARD_TIME_OUT = MEMPACK_PAY_ERROR + 6;

	/**
	 * 会员卡已使用.
	 */
	public static final int MEMPACK_CARD_USED = MEMPACK_PAY_ERROR + 7;

	/**
	 * 账户试错操作过于频繁.
	 */
	public static final int MEMPACK_CARD_ERROR_FREQUENT = MEMPACK_PAY_ERROR + 8;

	/**
	 * 会员卡类型不匹配.
	 */
	public static final int MEMPACK_CARD_TYPE_ERROR = MEMPACK_PAY_ERROR + 9;

	public MemberPackagePaymentException() {
		super();
	}

	public MemberPackagePaymentException(int code, Object... args) {
		super(code, args);
	}

	public MemberPackagePaymentException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public MemberPackagePaymentException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}
}
