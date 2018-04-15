package com.lanking.cloud.domain.yoo.honor.userTask;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 用户任务状态
 * 
 * <pre>
 * 
 * INIT → PROCESS_DATA → OPEN → DELETED
 *             ↑           ↓       ↑
 *             -------- DISABLED ---
 * </pre>
 * 
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
public enum UserTaskStatus implements Valueable {
	/**
	 * 新建
	 */
	INIT(0),
	/**
	 * 处理数据(不能人工修改此状态)
	 */
	PROCESS_DATA(1),
	/**
	 * 开启
	 */
	OPEN(2),
	/**
	 * 停用
	 */
	DISABLED(3),
	/**
	 * 删除(状态不可改)
	 */
	DELETED(4);

	private final int value;

	private UserTaskStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
