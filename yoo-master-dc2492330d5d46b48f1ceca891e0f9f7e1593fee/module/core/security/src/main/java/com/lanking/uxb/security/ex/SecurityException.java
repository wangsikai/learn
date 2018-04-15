package com.lanking.uxb.security.ex;

import com.lanking.cloud.ex.base.AbstractBasicServiceException;

public class SecurityException extends AbstractBasicServiceException {
	private static final long serialVersionUID = -5934979658463333142L;

	static final int SECURITY_ERROR = 500;

	public static final int NEED_LOGIN = SECURITY_ERROR + 1;

	public SecurityException(int code, Object... args) {
		super(code, args);
	}
}
