package com.lanking.cloud.domain.common.resource.card;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 卡片状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum CardStatus implements Valueable {
	/**
	 * 草稿
	 */
	DRAFT(0),
	/**
	 * 未校验.
	 */
	EDITING(1),
	/**
	 * 已通过.
	 */
	PASS(2),
	/**
	 * 不通过.
	 */
	NOPASS(3);

	private int value;

	CardStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public String getName() {
		switch (value) {
		case 0:
			return "草稿";
		case 1:
			return "未校验";
		case 2:
			return "已通过";
		case 3:
			return "不通过";
		default:
			return "未校验";
		}
	}

	public static CardStatus findByValue(int value) {
		switch (value) {
		case 0:
			return DRAFT;
		case 1:
			return EDITING;
		case 2:
			return PASS;
		case 3:
			return NOPASS;
		default:
			return EDITING;
		}
	}
}
