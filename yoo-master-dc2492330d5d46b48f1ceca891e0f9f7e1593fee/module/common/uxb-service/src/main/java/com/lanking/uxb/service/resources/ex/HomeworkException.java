package com.lanking.uxb.service.resources.ex;

import com.lanking.cloud.ex.AbstractException;

public class HomeworkException extends AbstractException {

	private static final long serialVersionUID = -2073474868324260864L;

	static final int HOMEWORK_ERROR = 900;
	/**
	 * 作业已经发布,不能重新设置时间
	 */
	public static final int HOMEWORK_HAS_PUBLISH = HOMEWORK_ERROR + 1;
	/**
	 * 作业时间非法
	 */
	public static final int HOMEWORK_TIME_ILLEGAL = HOMEWORK_ERROR + 2;
	/**
	 * 作业不存在
	 */
	public static final int HOMEWORK_NOT_EXIST = HOMEWORK_ERROR + 3;
	/**
	 * 不能查看作业批改结果
	 */
	public static final int HOMEWORK_CANNOT_VIEWRESULT = HOMEWORK_ERROR + 4;
	/**
	 * 作业里面习题已经存在
	 */
	public static final int HOMEWORK_QUESTION_EXIST = HOMEWORK_ERROR + 5;
	/**
	 * 作业没有批改完
	 */
	public static final int HOMEWORK_NOT_CORRECTED = HOMEWORK_ERROR + 6;
	/**
	 * 学生作业还没有提交
	 */
	public static final int HOMEWORK_NOT_COMMITED = HOMEWORK_ERROR + 7;
	/**
	 * 作业已经下发
	 */
	public static final int HOMEWORK_HAS_ISSUE = HOMEWORK_ERROR + 8;

	public HomeworkException() {
		super();
	}

	public HomeworkException(int code, Object... args) {
		super(code, args);
	}

	public HomeworkException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public HomeworkException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}
}
