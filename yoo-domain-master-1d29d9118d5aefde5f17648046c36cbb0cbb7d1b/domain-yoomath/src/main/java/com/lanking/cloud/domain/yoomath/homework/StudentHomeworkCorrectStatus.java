package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 批改状态（教师视角）.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public enum StudentHomeworkCorrectStatus implements Valueable {

	/**
	 * 默认/初始化/未知.
	 */
	DEFAULT(0),

	/**
	 * 自动批改中（有自动批改的题目才会有此状态）.
	 */
	AUTO_CORRECTING(1),

	/**
	 * 批改中（自动批改完成且有待人工批改的题目）.
	 */
	CORRECTING(2),

	/**
	 * 待批改（自动批改完成且有解答题需要老师自行批改，此状态会与批改中会冲突，冲突时仅使用该状态）
	 */
	TOBE_CORRECTED(3),

	/**
	 * 正确率统计中.
	 */
	RIGHT_RATE_STAT(4),

	/**
	 * 批改完成（所有题目批改完毕）.
	 */
	COMPLETE(5);

	private int value;

	private StudentHomeworkCorrectStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static StudentHomeworkCorrectStatus findByValue(int value) {
		switch (value) {
		case 1:
			return AUTO_CORRECTING;
		case 2:
			return CORRECTING;
		case 3:
			return TOBE_CORRECTED;
		case 4:
			return RIGHT_RATE_STAT;
		case 5:
			return COMPLETE;
		default:
			return DEFAULT;
		}
	}
}
