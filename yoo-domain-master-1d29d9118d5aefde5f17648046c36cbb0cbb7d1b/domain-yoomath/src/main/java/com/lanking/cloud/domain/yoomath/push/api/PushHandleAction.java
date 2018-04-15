package com.lanking.cloud.domain.yoomath.push.api;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 触发推送的动作类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum PushHandleAction implements Valueable {
	/**
	 * 新作业
	 */
	NEW_HOMEWORK(0),
	/**
	 * 快过期作业
	 */
	OVERDUE_HOMEWORK(1),
	/**
	 * 被强制提交的作业
	 */
	FORCESUBMITTED_HOMEWORK(2),
	/**
	 * 下发作业
	 */
	ISSUED_HOMEWORK(3),
	/**
	 * 作业进入待批改状态
	 */
	HOMEWORK_TO_BE_CORRECT(4),
	/**
	 * 推迟截止时间
	 */
	DELAY_HOMEWORK_DEADLINE(5);

	private final int value;

	private PushHandleAction(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
