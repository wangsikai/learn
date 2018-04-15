package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class MissingArgumentException extends AbstractCoreException {

	private static final long serialVersionUID = -1631576836686360188L;

	public MissingArgumentException() {
		super(MISSING_ARG_EX);
	}

	public MissingArgumentException(String argName) {
		super(MISSING_ARG_EX, argName);
	}

	public MissingArgumentException(String argName, Throwable cause) {
		super(cause, MISSING_ARG_EX, argName);
	}
}