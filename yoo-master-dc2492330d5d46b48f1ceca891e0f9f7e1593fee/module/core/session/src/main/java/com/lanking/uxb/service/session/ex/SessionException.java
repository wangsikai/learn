package com.lanking.uxb.service.session.ex;

import com.lanking.cloud.ex.base.AbstractBasicServiceException;

public class SessionException extends AbstractBasicServiceException {

	private static final long serialVersionUID = -9163649969635620235L;

	static final int SESSION_ERROR = 100;

	// 无效会话
	public static final int SESSION_INVALID = SESSION_ERROR + 1;

	public SessionException(int code, Object... args) {
		super(code, args);
	}
}
