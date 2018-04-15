package com.lanking.cloud.domain.common.resource.teachAssist;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 教辅状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum TeachAssistStatus implements Valueable {

	/**
	 * 编辑中
	 */
	EDITING(0),

	/**
	 * 未校验
	 */
	NOCHECK(1),

	/**
	 * 已通过/待发布
	 */
	PASS(2),

	/**
	 * 未通过(继续编辑)
	 */
	NOPASS(3),

	/**
	 * 发布
	 */
	PUBLISH(4);

	private int value;

	TeachAssistStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static TeachAssistStatus findByValue(int value) {
		switch (value) {
		case 0:
			return EDITING;
		case 1:
			return NOCHECK;
		case 2:
			return PASS;
		case 3:
			return NOPASS;
		case 4:
			return PUBLISH;
		default:
			return null;
		}
	}
}
