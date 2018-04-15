package com.lanking.cloud.domain.yoo.customerService;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 客服信息读取状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CustomerLogReadStatus implements Valueable {
	/**
	 * 未读
	 */
	UNREAD(0),
	/**
	 * 已读
	 */
	READ(1);

	private int value;

	CustomerLogReadStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}
}
