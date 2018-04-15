package com.lanking.uxb.service.mall.ex;

import com.lanking.cloud.ex.AbstractException;

/**
 * 资源支付相关异常.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月5日
 */
public class ResourcesGoodsPaymentException extends AbstractException {
	private static final long serialVersionUID = -8544409564703919928L;

	private static final int RESGODDS_PAY_ERROR = 2800;

	/**
	 * 商品已下架或被删除.
	 */
	public static final int RESGODDS_UNPUBLISH = RESGODDS_PAY_ERROR + 1;

	/**
	 * 金币数量不足.
	 */
	public static final int RESGODDS_PAY_COINS_NOT_ENOUGH = RESGODDS_PAY_ERROR + 2;

	/**
	 * 该订单已被支付.
	 */
	public static final int RESGODDS_PAY_COMPLETE = RESGODDS_PAY_ERROR + 3;

	public ResourcesGoodsPaymentException() {
		super();
	}

	public ResourcesGoodsPaymentException(int code, Object... args) {
		super(code, args);
	}

	public ResourcesGoodsPaymentException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public ResourcesGoodsPaymentException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}
}
