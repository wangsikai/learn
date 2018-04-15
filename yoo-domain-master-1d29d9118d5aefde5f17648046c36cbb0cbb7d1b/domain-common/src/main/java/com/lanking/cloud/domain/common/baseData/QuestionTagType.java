package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

public enum QuestionTagType implements Valueable {
	SYSTEM(0), MANUAL(1);

	private int value;

	private QuestionTagType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
