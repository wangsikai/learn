package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 科举考试的年级
 * 
 * @since 3.9.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月29日
 */
public enum ImperialExaminationGrade implements Valueable {
	/**
	 * 初一
	 */
	PHASE_2_1(0, 2, "初一"),
	/**
	 * 初二
	 */
	PHASE_2_2(1, 2, "初二"),
	/**
	 * 初三
	 */
	PHASE_2_3(2, 2, "初三");

	private int value;
	private int phase;
	private String name;

	ImperialExaminationGrade(int value, int phase, String name) {
		this.value = value;
		this.phase = phase;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	public int getPhase() {
		return phase;
	}

	public String getName() {
		return name;
	}

}
