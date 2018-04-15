package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 题目状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum CheckStatus implements Valueable {
	/**
	 * 未校验.
	 */
	EDITING(0),
	/**
	 * 校验中.
	 */
	CHECKING(1),
	/**
	 * 通过（二校/最终校验通过）.
	 */
	PASS(2),
	/**
	 * 未通过.
	 */
	NOPASS(3),

	/**
	 * 一校通过.
	 */
	ONEPASS(4),
	/**
	 * 草稿
	 */
	DRAFT(5);

	private int value;

	CheckStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public String getName() {
		switch (value) {
		case 0:
			return "未校验";
		case 1:
			return "校验中";
		case 2:
			return "已通过";
		case 3:
			return "未通过";
		case 4:
			return "一校通过";
		case 5:
			return "草稿";
		default:
			return "未校验";
		}
	}

	public static CheckStatus findByValue(int value) {
		switch (value) {
		case 0:
			return EDITING;
		case 1:
			return CHECKING;
		case 2:
			return PASS;
		case 3:
			return NOPASS;
		case 4:
			return ONEPASS;
		case 5:
			return DRAFT;
		default:
			return EDITING;
		}
	}
}
