package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 申请状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum ApplyStatus implements Valueable {
	/**
	 * 申请
	 */
	APPLY(0),
	/**
	 * 同意
	 */
	AGREE(1),
	/**
	 * 拒绝
	 */
	REFUSE(2);

	private int value;

	ApplyStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static ApplyStatus findByValue(int value) {
		switch (value) {
		case 0:
			return APPLY;
		case 1:
			return AGREE;
		case 2:
			return REFUSE;
		default:
			return null;
		}
	}

}
