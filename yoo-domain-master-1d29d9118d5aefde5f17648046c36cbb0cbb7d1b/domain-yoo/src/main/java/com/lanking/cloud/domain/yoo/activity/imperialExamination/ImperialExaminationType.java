package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 科举考试类型
 * 
 * @since 3.9.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月29日
 */
public enum ImperialExaminationType implements Valueable {
	/**
	 * 乡试
	 */
	PROVINCIAL_EXAMINATION(0, "乡试"),
	/**
	 * 会试
	 */
	METROPOLITAN_EXAMINATION(1, "会试"),
	/**
	 * 殿试
	 */
	FINAL_IMPERIAL_EXAMINATION(2, "殿试");

	private int value;
	private String name;

	ImperialExaminationType(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

}
