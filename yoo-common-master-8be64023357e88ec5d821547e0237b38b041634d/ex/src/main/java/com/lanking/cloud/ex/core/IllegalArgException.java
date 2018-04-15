package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class IllegalArgException extends AbstractCoreException {

	private static final long serialVersionUID = -1809933545551282974L;

	public IllegalArgException() {
		super(ILLEGAL_ARG_EX);
	}

	public IllegalArgException(String argName) {
		super(ILLEGAL_ARG_EX, argName);
	}

	public IllegalArgException(String argName, Throwable cause) {
		super(cause, ILLEGAL_ARG_EX, argName);
	}
}
