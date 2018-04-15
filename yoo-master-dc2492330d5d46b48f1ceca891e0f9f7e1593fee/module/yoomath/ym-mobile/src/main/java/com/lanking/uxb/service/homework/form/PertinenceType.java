package com.lanking.uxb.service.homework.form;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 知识点巩固练习拉取题目，类型
 *
 * @author xinyu.zhou
 * @since 3.9.2
 */
public enum PertinenceType implements Valueable {

	// 基础
	BASE(0),

	// 提高
	RAISE(1),

	// 冲刺
	SPRINT(2);

	private int value;

	@Override
	public int getValue() {
		return this.value;
	}

	PertinenceType(int value) {
		this.value = value;
	}
}
