package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业留言场景.
 * 
 * @since 小优快批
 *
 */
public enum HomeworkMessageType implements Valueable {

	/**
	 * 文本.
	 */
	TEXT(0),

	/**
	 * 音频.
	 */
	AUDIO(1),

	/**
	 * 视频.
	 */
	VIDEO(2);

	private final int value;

	private HomeworkMessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static HomeworkMessageType findByValue(int value) {
		switch (value) {
		case 0:
			return TEXT;
		case 1:
			return AUDIO;
		case 2:
			return VIDEO;
		default:
			return TEXT;
		}
	}
}
