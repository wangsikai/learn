package com.lanking.uxb.service.resources.ex;

import com.lanking.cloud.ex.AbstractException;

public class QuestionException extends AbstractException {

	private static final long serialVersionUID = -2073474868324260864L;

	static final int QUESTION_ERROR = 950;

	/**
	 * 练习不存在
	 */
	public static int QUESTION_NOT_EXIST = QUESTION_ERROR + 1;

	public QuestionException() {
		super();
	}

	public QuestionException(int code, Object... args) {
		super(code, args);
	}

	public QuestionException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public QuestionException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}
}
