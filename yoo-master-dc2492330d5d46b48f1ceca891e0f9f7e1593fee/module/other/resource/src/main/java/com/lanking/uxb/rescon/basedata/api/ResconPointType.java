package com.lanking.uxb.rescon.basedata.api;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 定义知识点类型->(知识点、元知识点)
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public enum ResconPointType implements Valueable {
	KNOWPOINT(0),

	METAKNOWPOINT(1);

	private final int value;

	ResconPointType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}
}
