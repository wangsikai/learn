package com.lanking.cloud.domain.support.channelSales.user;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 渠道商-用户操作类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum ChannelUserOperateLogType implements Valueable {
	/**
	 * 未知.
	 */
	NULL(0),
	/**
	 * 重置密码
	 */
	RESETPASSWORD(1);

	private int value;

	ChannelUserOperateLogType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static ChannelUserOperateLogType findByValue(int value) {
		switch (value) {
		case 0:
			return NULL;
		case 1:
			return RESETPASSWORD;
		default:
			return NULL;
		}
	}
}
