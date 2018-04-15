package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum HomeworkStatus implements Valueable {
	/**
	 * 新建（待分发）.
	 */
	INIT(0),

	/**
	 * 发布（作业中）.
	 */
	PUBLISH(1),

	/**
	 * 未下发（已截止，不再表示旧流程的待批改）
	 * 
	 * @since 小悠快批，2018-2-27，原状态应用于新流程的“已截止”，同时代表所有学生作业均已提交
	 */
	NOT_ISSUE(2),

	/**
	 * 已下发.
	 * 
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 */
	@Deprecated
	ISSUED(3);

	private final int value;

	private HomeworkStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static HomeworkStatus findByValue(int value) {
		switch (value) {
		case 0:
			return INIT;
		case 1:
			return PUBLISH;
		case 2:
			return NOT_ISSUE;
		case 3:
			return ISSUED;
		default:
			return null;
		}
	}
}
