package com.lanking.cloud.domain.yoo.honor.userTask;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 用户任务针对的用户范围
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
public enum UserTaskUserScope implements Valueable {
	/**
	 * 所有
	 */
	ALL(0),
	/**
	 * 自主注册
	 */
	SELF_REGISTRATION(1),
	/**
	 * 渠道用户
	 */
	CHANNEL_USER(2);

	private final int value;

	private UserTaskUserScope(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
