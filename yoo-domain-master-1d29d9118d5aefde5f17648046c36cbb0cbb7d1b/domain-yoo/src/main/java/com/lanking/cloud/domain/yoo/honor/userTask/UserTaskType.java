package com.lanking.cloud.domain.yoo.honor.userTask;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 用户任务类型
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
public enum UserTaskType implements Valueable {
	/**
	 * 新用户任务
	 */
	NEW_USER(0),
	/**
	 * 每日任务
	 */
	DAILY(1),
	/**
	 * 成就任务
	 */
	ACHIEVEMENT(2),
	/**
	 * 隐式任务
	 */
	HIDDEN(3);

	private final int value;

	private UserTaskType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		switch (this) {
		case NEW_USER:
			return "新手任务";
		case DAILY:
			return "每日任务";
		case ACHIEVEMENT:
			return "成就任务";
		case HIDDEN:
			return "隐式任务";
		}

		return "";
	}

	public static UserTaskType findByValue(int value) {
		switch (value) {
		case 0:
			return NEW_USER;
		case 1:
			return DAILY;
		case 2:
			return ACHIEVEMENT;
		case 3:
			return HIDDEN;
		}

		return null;
	}
}
