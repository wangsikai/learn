package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 资源类别代码,对应resource_category表中的一级类别代码
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 * @see ResourceCategory
 */
public enum ResourceCategoryCode implements Valueable {
	NULL(0),
	/**
	 * 试题试卷
	 */
	EXAMINATION_PAPER(1),
	/**
	 * 
	 */
	UNDEFINED_1(2),
	/**
	 * 
	 */
	UNDEFINED_2(3),
	/**
	 * 
	 */
	UNDEFINED_3(4),
	/**
	 * 书本
	 */
	BOOK(5);

	private int value;

	ResourceCategoryCode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
