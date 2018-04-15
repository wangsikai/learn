package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class IllegalArgFormatException extends AbstractCoreException {

	private static final long serialVersionUID = -3218471479676565277L;

	public IllegalArgFormatException() {
		super(ILLEGAL_ARG_FORMAT_EX);
	}

	public IllegalArgFormatException(String argName) {
		super(ILLEGAL_ARG_FORMAT_EX, argName);
	}

	public IllegalArgFormatException(String argName, Throwable cause) {
		super(cause, ILLEGAL_ARG_FORMAT_EX, argName);
	}
}
