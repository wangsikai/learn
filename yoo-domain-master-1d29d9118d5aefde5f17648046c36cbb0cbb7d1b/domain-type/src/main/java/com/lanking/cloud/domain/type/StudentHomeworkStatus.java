package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 学生作业状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum StudentHomeworkStatus implements Valueable {
	NOT_SUBMIT(0), SUBMITED(1),

	/**
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 */
	@Deprecated
	ISSUED(2);

	private final int value;

	private StudentHomeworkStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static StudentHomeworkStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOT_SUBMIT;
		case 1:
			return SUBMITED;
		case 2:
			return ISSUED;
		default:
			return null;
		}
	}
}
