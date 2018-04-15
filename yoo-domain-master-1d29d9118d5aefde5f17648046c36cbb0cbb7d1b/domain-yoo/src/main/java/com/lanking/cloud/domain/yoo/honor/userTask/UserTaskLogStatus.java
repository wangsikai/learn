package com.lanking.cloud.domain.yoo.honor.userTask;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 用户任务记录状态
 * 
 * <pre>
 * 
 * TASKING → COMPLETE → RECEIVE → DELETED
 *    ↓          ↓                   ↑
 * DISABLED  DISABLED             DISABLED
 * </pre>
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
public enum UserTaskLogStatus implements Valueable {
	/**
	 * 任务状态
	 */
	TASKING(0),
	/**
	 * 完成状态
	 */
	COMPLETE(1),
	/**
	 * 领取
	 */
	RECEIVE(2),
	/**
	 * 停用(任务停用后对应的用户记录也应该被停用,在下次启动时更新老数据)
	 */
	DISABLED(3),
	/**
	 * 删除(暂时无用)
	 */
	DELETED(4);

	private final int value;

	private UserTaskLogStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
