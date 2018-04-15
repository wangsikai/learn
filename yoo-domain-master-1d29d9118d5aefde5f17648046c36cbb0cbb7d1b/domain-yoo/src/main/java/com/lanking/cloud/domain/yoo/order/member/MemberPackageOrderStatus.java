package com.lanking.cloud.domain.yoo.order.member;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员套餐订单状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberPackageOrderStatus implements Valueable {

	/**
	 * 未支付
	 */
	NOT_PAY(0),
	/**
	 * 已支付
	 */
	PAY(1),
	/**
	 * 后台处理中
	 */
	PROCESSING(2),
	/**
	 * 完成
	 */
	COMPLETE(3),
	/**
	 * 失败
	 */
	FAIL(4),
	/**
	 * 删除
	 */
	DELETE(5),
	/**
	 * 取消
	 */
	CANCEL(6);

	private final int value;

	private MemberPackageOrderStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MemberPackageOrderStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOT_PAY;
		case 1:
			return PAY;
		case 2:
			return PROCESSING;
		case 3:
			return COMPLETE;
		case 4:
			return FAIL;
		case 5:
			return DELETE;
		default:
			return null;
		}
	}
}
