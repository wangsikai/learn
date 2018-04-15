package com.lanking.uxb.service.adminSecurity.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public enum ConsoleOperateType implements Valueable {
	ADD(0),

	REMOVE(1);

	private final int value;

	private ConsoleOperateType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
