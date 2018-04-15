package com.lanking.cloud.domain.yoo.order.fallible;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 错题打印订单状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum FallibleQuestionPrintOrderStatus implements Valueable {
	/**
	 * 未支付
	 */
	NOT_PAY(0),

	/**
	 * 已支付/后台待处理
	 */
	PAY(1),

	/**
	 * 打印处理中
	 */
	PRINTING(2),

	/**
	 * 打印完成
	 */
	PRINT_COMPLETE(3),

	/**
	 * 已发货
	 */
	DELIVERED(4),

	/**
	 * 已收货/订单完成
	 */
	COMPLETE(5),

	/**
	 * 失败
	 */
	FAIL(6),

	/**
	 * 删除
	 */
	DELETE(7);

	private int value;

	FallibleQuestionPrintOrderStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static FallibleQuestionPrintOrderStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOT_PAY;
		case 1:
			return PAY;
		case 2:
			return PRINTING;
		case 3:
			return PRINT_COMPLETE;
		case 4:
			return DELIVERED;
		case 5:
			return COMPLETE;
		case 6:
			return FAIL;
		case 7:
			return DELETE;
		default:
			return NOT_PAY;
		}
	}

}
