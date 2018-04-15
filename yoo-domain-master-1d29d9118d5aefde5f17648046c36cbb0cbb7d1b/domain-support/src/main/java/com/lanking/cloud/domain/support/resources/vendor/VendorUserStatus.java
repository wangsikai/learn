package com.lanking.cloud.domain.support.resources.vendor;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 供应商管理员用户状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum VendorUserStatus implements Valueable {
	/**
	 * 初始化
	 */
	INIT(0),
	/**
	 * 正常可用状态
	 */
	ENABLED(1),
	/**
	 * 正常不可用状态
	 */
	DISABLED(2),
	/**
	 * 删除状态
	 */
	DELETED(3);

	private final int value;

	private VendorUserStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static VendorUserStatus findByValue(int value) {
		switch (value) {
		case 0:
			return INIT;
		case 1:
			return ENABLED;
		case 2:
			return DISABLED;
		case 3:
			return DELETED;
		default:
			return null;
		}
	}
}
