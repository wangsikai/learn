package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 科举考试第二期的考试逻辑需要
 * 
 * @since 3.9.4
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 * @version 2017年11月17日
 */
public enum ImperialExaminationExamTag implements Valueable {
	/**
	 * 首次成绩
	 */
	FIRST_EXAM(1, "首次成绩"),
	/**
	 * 冲刺1
	 */
	SECOND_EXAM(2, "冲刺1"),
	/**
	 * 冲刺2
	 */
	THIRD_EXAM(3, "冲刺2");

	private int value;
	private String name;

	ImperialExaminationExamTag(int value, String name) {
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
