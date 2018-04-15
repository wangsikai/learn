package com.lanking.cloud.domain.common.resource.examPaper;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 题目类型分类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum ExamPaperTopicType implements Valueable {

	/**
	 * 单选
	 */
	SINGLE_CHOICE(0),
	/**
	 * 多选
	 */
	MULTIPLE_CHOICE(1),
	/**
	 * 填空
	 */
	FILL_BLANK(2),
	/**
	 * 解答
	 */
	QUESTION_ANSWERING(3),
	/**
	 * 判断题
	 */
	TRUE_OR_FALSE(4);

	private int value;

	ExamPaperTopicType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
