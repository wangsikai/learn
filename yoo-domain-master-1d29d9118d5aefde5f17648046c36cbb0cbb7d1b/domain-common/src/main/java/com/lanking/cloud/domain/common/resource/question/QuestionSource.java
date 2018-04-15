package com.lanking.cloud.domain.common.resource.question;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 题目来源
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum QuestionSource implements Valueable {

	/**
	 * 供应商系统
	 */
	VENDOR(0),
	/**
	 * 资源平台系统
	 */
	RESCON(1);

	private int value;

	QuestionSource(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static QuestionSource findByValue(int value) {
		switch (value) {
		case 0:
			return VENDOR;
		case 1:
			return RESCON;
		default:
			return VENDOR;
		}
	}
}
