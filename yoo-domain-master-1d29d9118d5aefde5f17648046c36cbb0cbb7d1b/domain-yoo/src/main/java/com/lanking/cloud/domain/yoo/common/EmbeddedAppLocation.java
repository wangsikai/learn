package com.lanking.cloud.domain.yoo.common;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 内嵌应用(app,web上的入口) 位置定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum EmbeddedAppLocation implements Valueable {
	/**
	 * 应用首页
	 */
	HOME(0);

	private int value;

	EmbeddedAppLocation(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
