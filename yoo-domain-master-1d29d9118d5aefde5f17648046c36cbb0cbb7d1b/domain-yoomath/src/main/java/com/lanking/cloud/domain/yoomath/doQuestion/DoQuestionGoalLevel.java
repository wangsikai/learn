package com.lanking.cloud.domain.yoomath.doQuestion;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 刷题目标级别
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum DoQuestionGoalLevel {
	/**
	 * LEVEL_0
	 */
	LEVEL_0(0, -1, StringUtils.EMPTY, "/images/mobile/doQuestion/goal/LEVEL_0.png"),
	/**
	 * LEVEL_1
	 */
	LEVEL_1(1, 10, "学民", "/images/mobile/doQuestion/goal/LEVEL_1.png"),
	/**
	 * LEVEL_2
	 */
	LEVEL_2(2, 20, "学神", "/images/mobile/doQuestion/goal/LEVEL_2.png"),
	/**
	 * LEVEL_3
	 */
	LEVEL_3(3, 30, "学霸", "/images/mobile/doQuestion/goal/LEVEL_3.png"),
	/**
	 * LEVEL_4
	 */
	LEVEL_4(4, 40, "学魔", "/images/mobile/doQuestion/goal/LEVEL_4.png");

	private final int value;
	private final int goal;
	private final String name;
	private final String url;

	DoQuestionGoalLevel(int value, int goal, String name, String url) {
		this.value = value;
		this.goal = goal;
		this.name = name;
		this.url = url;

	}

	public int getValue() {
		return value;
	}

	public int getGoal() {
		return goal;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public DoQuestionGoalLevel findByValue(int value) {
		switch (value) {
		case 0:
			return LEVEL_0;
		case 2:
			return LEVEL_1;
		case 3:
			return LEVEL_2;
		case 4:
			return LEVEL_3;
		case 5:
			return LEVEL_4;
		}
		return null;
	}
}
