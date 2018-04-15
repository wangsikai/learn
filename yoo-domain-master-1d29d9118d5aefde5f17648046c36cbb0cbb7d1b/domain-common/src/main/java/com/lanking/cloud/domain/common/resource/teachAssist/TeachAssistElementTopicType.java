package com.lanking.cloud.domain.common.resource.teachAssist;

import com.lanking.cloud.sdk.bean.Valueable;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 教辅专题模块-专题类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum TeachAssistElementTopicType implements Valueable {
	/**
	 * 知识专题
	 */
	POINT(0),

	/**
	 * 规律方法
	 */
	METHOD(1),

	/**
	 * 数学思想
	 */
	MATH(2);

	private int value;

	TeachAssistElementTopicType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static TeachAssistElementTopicType findByValue(int value) {
		switch (value) {
		case 0:
			return POINT;
		case 1:
			return METHOD;
		case 2:
			return MATH;
		default:
			return null;
		}
	}

	public String getName() {
		switch (this) {
		case POINT:
			return "知识专题";
		case METHOD:
			return "规律方法";
		case MATH:
			return "数学思想";
		default:
			return StringUtils.EMPTY;
		}
	}
}
