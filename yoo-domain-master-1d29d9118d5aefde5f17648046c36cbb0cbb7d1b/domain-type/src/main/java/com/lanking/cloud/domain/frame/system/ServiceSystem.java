package com.lanking.cloud.domain.frame.system;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 服务系统
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum ServiceSystem implements Valueable {
	/**
	 * 默认
	 */
	NULL(0);

	private final int value;

	private ServiceSystem(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ServiceSystem findByValue(int value) {
		switch (value) {
		case 0:
			return NULL;
		default:
			return null;
		}
	}
}
