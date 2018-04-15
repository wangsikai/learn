package com.lanking.cloud.domain.yoomath.examPaper;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 组卷类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CustomExampaperType implements Valueable {

	/**
	 * 手工
	 */
	MANUAL(0),

	/**
	 * 智能
	 */
	SMART(1);

	private int value;

	CustomExampaperType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static CustomExampaperType findByValue(int value) {
		switch (value) {
		case 0:
			return MANUAL;
		case 1:
			return SMART;
		}
		return null;
	}
}
