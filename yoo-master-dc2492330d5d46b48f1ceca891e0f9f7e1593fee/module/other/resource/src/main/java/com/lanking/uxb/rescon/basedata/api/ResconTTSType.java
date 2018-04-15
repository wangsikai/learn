package com.lanking.uxb.rescon.basedata.api;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 章节管理里zTree节点类型
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public enum ResconTTSType implements Valueable {
	TEXTBOOKCATEGORY(0),

	TEXTBOOK(1),

	SECTION(2);

	private final int value;

	@Override
	public int getValue() {
		return value;
	}

	ResconTTSType(int value) {
		this.value = value;
	}
}
