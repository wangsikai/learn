package com.lanking.cloud.domain.support.resources.vendor;

/**
 * 供应商用户统计类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum VendorUserStatisType {
	/**
	 * 总的统计
	 */
	DETAIL(0),
	/**
	 * 按天的记录
	 */
	DAY_DETAIL(1);

	private final int value;

	private VendorUserStatisType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static VendorUserStatisType findByValue(int value) {
		switch (value) {
		case 0:
			return DETAIL;
		case 1:
			return DAY_DETAIL;
		default:
			return null;
		}
	}
}
