package com.lanking.cloud.domain.yoomath.examPaper;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 组卷题目类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CustomExampaperTopicType implements Valueable {

	/**
	 * 单选
	 */
	SINGLE_CHOICE(0, "选择题"),

	/**
	 * 填空
	 */
	FILL_BLANK(1, "填空题"),

	/**
	 * 解答
	 */
	QUESTION_ANSWERING(2, "解答题");

	private int value;
	private String name;

	CustomExampaperTopicType(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}
