package com.lanking.cloud.domain.yoomath.interaction;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 师生互动状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum InteractionStatus implements Valueable {
	/**
	 * 批评
	 */
	CRITICISM(0),
	/**
	 * 未发送表扬
	 */
	UNSENT_PRAISE(1),
	/**
	 * 已发送表扬
	 */
	SENT_PRAISE(2),
	/**
	 * 忽略
	 */
	IGNORE_PRAISE(3),
	/**
	 * 删除
	 */
	DELETE(4);

	private final int value;

	private InteractionStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static InteractionStatus findByValue(int value) {
		switch (value) {
		case 0:
			return CRITICISM;
		case 1:
			return UNSENT_PRAISE;
		case 2:
			return SENT_PRAISE;
		case 3:
			return IGNORE_PRAISE;
		case 4:
			return DELETE;
		default:
			return null;
		}
	}
}
