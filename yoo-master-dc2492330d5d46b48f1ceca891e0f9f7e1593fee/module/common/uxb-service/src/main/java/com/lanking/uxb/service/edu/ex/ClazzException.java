package com.lanking.uxb.service.edu.ex;

import com.lanking.cloud.ex.AbstractException;

public class ClazzException extends AbstractException {

	private static final long serialVersionUID = -1732560371189013025L;

	static final int CLAZZ_ERROR = 1100;

	/**
	 * 班级未找到
	 */
	public final static int CLAZZ_NOT_FOUND = CLAZZ_ERROR + 1;

	/**
	 * 一个班级成员只能担任一个班级职务
	 */
	public final static int HAS_BEEN_ADMIN = CLAZZ_ERROR + 2;
	/**
	 * 该班级职务已被任命
	 */
	public final static int DUTY_HAS_APPOINTED = CLAZZ_ERROR + 3;

	/**
	 * 该班级简介长度超出限制
	 */
	public final static int INTRO_LENGTH_OVER = CLAZZ_ERROR + 4;

	/**
	 * 班级已删除或关闭
	 * 
	 * @since 2.0.3
	 */
	public final static int CLASS_CLOSE_DELTE = CLAZZ_ERROR + 5;

	/**
	 * 学生已被移出班级
	 * 
	 * @since 2.0.3
	 */
	public final static int STUDENT_REMOVE_CLASS = CLAZZ_ERROR + 6;

	public ClazzException() {
		super();
	}

	public ClazzException(int code, Object... args) {
		super(code, args);
	}

	public ClazzException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public ClazzException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}
}
