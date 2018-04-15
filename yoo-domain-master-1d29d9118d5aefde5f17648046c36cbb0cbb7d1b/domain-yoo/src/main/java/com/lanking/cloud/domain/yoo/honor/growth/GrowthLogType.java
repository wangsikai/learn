package com.lanking.cloud.domain.yoo.honor.growth;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 成长值记录类型
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月4日
 */
public enum GrowthLogType implements Valueable {
	/**
	 * growth_rule
	 */
	GROWTH_RULE(0),
	/**
	 * user_task
	 */
	USER_TASK(1);

	private final int value;

	private GrowthLogType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
