package com.lanking.cloud.domain.common.resource.question;

import com.lanking.cloud.sdk.bean.Valueable;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 题目类别
 * 
 * <pre>
 * 例题：若题目在知识卡片中被设定为例题，则出现例题标签 典型题：若题目在考点中被设定为典型题，则出现典型题标签
 * 模拟：若题目出现在模拟试卷中，则出现模拟标签 真题：若题目出现在中高考试卷中，则出现真题标标签
 * 压轴题：若题目出现在模拟/中高考卷中，且为卷中最后两题，则出现压轴题标签
 * </pre>
 * 
 * @since 3.9.3
 * @since 教师端 v1.3.0 2017-8-2 废弃使用
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Deprecated
public enum QuestionCategoryType implements Valueable {
	/**
	 * 例题
	 */
	EXAMPLE(0),

	/**
	 * 典型题
	 */
	TYPICAL(1),

	/**
	 * 模拟题
	 */
	SIMULATION(2),

	/**
	 * 真题
	 */
	TRUTH(3),

	/**
	 * 压轴题
	 */
	FINALE(4);

	private int value;

	QuestionCategoryType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public QuestionCategoryType findByValue(int value) {
		switch (value) {
		case 0:
			return EXAMPLE;
		case 1:
			return TYPICAL;
		case 2:
			return SIMULATION;
		case 3:
			return TRUTH;
		case 4:
			return FINALE;
		}
		return null;
	}

	public String getName() {
		switch (this) {
		case EXAMPLE:
			return "例题";
		case TYPICAL:
			return "典型题";
		case SIMULATION:
			return "模拟题";
		case TRUTH:
			return "真题";
		case FINALE:
			return "压轴题";
		}
		return StringUtils.EMPTY;
	}
}
