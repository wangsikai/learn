package com.lanking.cloud.domain.base.message.api;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 消息类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public enum MessageType implements Valueable {
	/**
	 * 私信
	 */
	PRIVATE(0),
	/**
	 * 邮件
	 */
	EMAIL(1),
	/**
	 * 短信
	 */
	SMS(2),
	/**
	 * 推送
	 */
	PUSH(3),
	/**
	 * 提醒
	 */
	NOTICE(4);

	private int value;

	MessageType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static MessageType findByValue(int value) {
		switch (value) {
		case 0:
			return PRIVATE;
		case 1:
			return EMAIL;
		case 2:
			return SMS;
		case 3:
			return PUSH;
		case 4:
			return NOTICE;
		default:
			return null;
		}
	}
}
