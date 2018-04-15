package com.lanking.uxb.service.sensitive.ex;

import com.lanking.cloud.ex.AbstractException;

/**
 * 敏感词相关异常
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年4月15日
 */
public class SensitiveException extends AbstractException {

	private static final long serialVersionUID = -1715791537410693453L;

	static final int SENSITIVE_ERROR = 1300;

	/**
	 * 包含禁止词
	 */
	public static final int CONTAIN_FORBIDDEN_WORD = SENSITIVE_ERROR + 1;

	public SensitiveException() {
		super();
	}

	public SensitiveException(int code, Object... args) {
		super(code, args);
	}

	public SensitiveException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public SensitiveException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}

}
