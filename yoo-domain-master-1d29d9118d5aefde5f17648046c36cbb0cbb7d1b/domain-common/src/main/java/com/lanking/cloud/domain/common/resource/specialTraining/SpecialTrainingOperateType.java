package com.lanking.cloud.domain.common.resource.specialTraining;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 专项训练的操作类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum SpecialTrainingOperateType implements Valueable {
	/**
	 * 未知.
	 */
	NULL(0),
	/**
	 * 创建训练.
	 */
	CREATE(1),
	/**
	 * 编辑训练.
	 */
	EDIT(2),
	/**
	 * 发布训练.
	 */
	PUBLISH(3);

	private int value;

	SpecialTrainingOperateType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static SpecialTrainingOperateType findByValue(int value) {
		switch (value) {
		case 0:
			return NULL;
		case 1:
			return CREATE;
		case 2:
			return EDIT;
		case 3:
			return PUBLISH;
		default:
			return NULL;
		}
	}
}
