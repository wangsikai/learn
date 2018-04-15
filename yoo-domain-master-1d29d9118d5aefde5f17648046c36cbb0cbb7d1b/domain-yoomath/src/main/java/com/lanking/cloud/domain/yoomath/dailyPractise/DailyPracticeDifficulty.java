package com.lanking.cloud.domain.yoomath.dailyPractise;

/**
 * 每日练难度类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum DailyPracticeDifficulty {
	/**
	 * 简单
	 */
	LEVEL_1(0, "（0.8-1.0】", "简单", 2),
	/**
	 * 普通,适中
	 */
	LEVEL_2(1, "（0.6-0.8】", "适中", 3),
	/**
	 * 较难
	 */
	LEVEL_3(2, "（0.3-0.6】", "较难", 4),
	/**
	 * 非常难
	 */
	LEVEL_4(3, "（0-0.3】", "非常难", 5);

	private int value;
	private String difficult;
	private String name;
	private int star;

	private DailyPracticeDifficulty(int value, String difficult, String name, int star) {
		this.value = value;
		this.difficult = difficult;
		this.name = name;
		this.star = star;
	}

	public int getValue() {
		return value;
	}

	public String getDifficult() {
		return difficult;
	}

	public String getName() {
		return name;
	}

	public int getStar() {
		return star;
	}

}
