package com.lanking.uxb.service.diagnostic.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 难度区间范围
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public enum QuestionDifficultyType implements Valueable {
	// 简单题 [0.8,1]
	SIMPLE(0),

	// 提高题 [0.4,0.8)
	MIDDLE(1),

	// 冲刺题 [0,0.4)
	DIFFICULT(2);

	private final int value;

	private QuestionDifficultyType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
