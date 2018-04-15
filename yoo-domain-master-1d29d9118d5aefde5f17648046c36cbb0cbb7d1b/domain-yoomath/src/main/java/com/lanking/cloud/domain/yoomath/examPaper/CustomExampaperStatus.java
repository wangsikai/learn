package com.lanking.cloud.domain.yoomath.examPaper;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 组卷状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CustomExampaperStatus implements Valueable {

	/**
	 * 草稿
	 */
	DRAFT(0),

	/**
	 * 正式
	 */
	ENABLED(1),

	/**
	 * 开卷
	 */
	OPEN(2),

	/**
	 * 删除
	 */
	DELETE(3);

	private int value;

	CustomExampaperStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static CustomExampaperStatus findByValue(int value) {
		switch (value) {
		case 0:
			return DRAFT;
		case 1:
			return ENABLED;
		case 2:
			return OPEN;
		case 3:
			return DELETE;
		}

		return null;
	}
}
