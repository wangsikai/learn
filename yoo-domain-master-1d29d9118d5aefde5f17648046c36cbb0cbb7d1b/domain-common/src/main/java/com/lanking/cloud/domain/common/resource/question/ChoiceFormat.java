package com.lanking.cloud.domain.common.resource.question;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 选项版式
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum ChoiceFormat implements Valueable {
	/**
	 * 水平排列 horizontal
	 */
	HORIZONTAL(0),
	/**
	 * 垂直排列vertical
	 */
	VERTICAL(1),
	/**
	 * 两项并列abreast
	 */
	ABREAST(2);

	private int value;

	ChoiceFormat(int value) {
		this.value = value;
	}

	public static ChoiceFormat findByValue(int value) {
		switch (value) {
		case 0:
			return HORIZONTAL;
		case 1:
			return VERTICAL;
		case 2:
			return ABREAST;
		default:
			return null;
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
