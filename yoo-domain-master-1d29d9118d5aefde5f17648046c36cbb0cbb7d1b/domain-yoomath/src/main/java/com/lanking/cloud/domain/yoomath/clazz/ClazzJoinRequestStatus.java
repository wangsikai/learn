package com.lanking.cloud.domain.yoomath.clazz;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 加入班级请求状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum ClazzJoinRequestStatus implements Valueable {
	/**
	 * 等待老师同意
	 */
	PROCESSING(0),
	/**
	 * 同意
	 */
	APPLY(1),
	/**
	 * 拒绝
	 */
	DENIED(2);

	private int value;

	ClazzJoinRequestStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public ClazzJoinRequestStatus findByValue(int value) {
		switch (value) {
		case 0:
			return PROCESSING;
		case 1:
			return APPLY;
		case 2:
			return DENIED;
		default:
			return PROCESSING;
		}
	}
}
