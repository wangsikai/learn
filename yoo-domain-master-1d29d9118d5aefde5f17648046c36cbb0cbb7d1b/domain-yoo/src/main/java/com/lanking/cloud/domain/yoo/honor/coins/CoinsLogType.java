package com.lanking.cloud.domain.yoo.honor.coins;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 金币记录类型
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月4日
 */
public enum CoinsLogType implements Valueable {
	/**
	 * coins_rule
	 */
	COINS_RULE(0),
	/**
	 * user_task
	 */
	USER_TASK(1);

	private final int value;

	private CoinsLogType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
