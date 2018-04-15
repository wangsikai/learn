package com.lanking.uxb.service.resources.ex;

import com.lanking.cloud.ex.AbstractException;

public class ExerciseException extends AbstractException {

	private static final long serialVersionUID = -2073474868324260864L;

	static final int EXERCISE_ERROR = 850;

	/**
	 * 练习名称不能为空
	 */
	public static int EXERCISE_NAME_BLANK = EXERCISE_ERROR + 1;
	/**
	 * 练习不存在
	 */
	public static int EXERCISE_NOT_EXIST = EXERCISE_ERROR + 2;
	/**
	 * 习题页中的习题已经存在
	 */
	public static int EXERCISE_QUESTION_EXIST = EXERCISE_ERROR + 3;
	/**
	 * 练习名称重复
	 */
	public static int EXERCISE_NAME_MULTIPLE = EXERCISE_ERROR + 4;

	public ExerciseException() {
		super();
	}

	public ExerciseException(int code, Object... args) {
		super(code, args);
	}

	public ExerciseException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public ExerciseException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}
}
