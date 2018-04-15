package com.lanking.cloud.domain.yoo.honor.userTask;

/**
 * 成就任务类别.
 * 
 * @author wlche
 * @since 3.9.7
 * 
 */
public enum UserTaskAchievementType {

	DEFAULT(0),

	/**
	 * 等级任务.
	 */
	LEVEL(1),

	/**
	 * 作业排名任务.
	 */
	HOMEWORK_RANK(2),

	/**
	 * 作业进步任务.
	 */
	HOMEWORK_ADVANCE(3),

	/**
	 * 自主练习任务.
	 */
	EXERCISE(4),

	/**
	 * 签到任务.
	 */
	SIGN_IN(5);

	private final int value;

	private UserTaskAchievementType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static UserTaskAchievementType findByValue(int value) {
		switch (value) {
			case 1 :
				return LEVEL;
			case 2 :
				return HOMEWORK_RANK;
			case 3 :
				return HOMEWORK_ADVANCE;
			case 4 :
				return EXERCISE;
			case 5 :
				return SIGN_IN;
			default :
				return DEFAULT;
		}
	}
}
