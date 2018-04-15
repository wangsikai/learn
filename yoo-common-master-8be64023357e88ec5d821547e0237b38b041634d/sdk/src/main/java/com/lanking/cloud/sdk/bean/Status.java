package com.lanking.cloud.sdk.bean;

/**
 * 通过状态枚举
 * 
 * <pre>
 *  此状态枚举为通用的状态，如果不满足的地方不可在此枚举中添加状态值，请另行创建新的状态.
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月15日
 */
public enum Status implements Valueable {
	/**
	 * 可用状态
	 */
	ENABLED(0),
	/**
	 * 不可用状态
	 */
	DISABLED(1),
	/**
	 * 删除状态(逻辑删除)
	 */
	DELETED(2);

	private final int value;

	private Status(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Status findByValue(int value) {
		switch (value) {
		case 0:
			return ENABLED;
		case 1:
			return DISABLED;
		case 2:
			return DELETED;
		default:
			return null;
		}
	}
}
