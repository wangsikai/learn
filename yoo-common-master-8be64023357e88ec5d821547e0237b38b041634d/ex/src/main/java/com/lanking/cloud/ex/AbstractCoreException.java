package com.lanking.cloud.ex;

import com.lanking.cloud.ex.code.core.CoreExceptionCode;

public abstract class AbstractCoreException extends AbstractException implements CoreExceptionCode {

	private static final long serialVersionUID = 1362981992374891843L;

	public AbstractCoreException() {
		super();
	}

	public AbstractCoreException(int code, Object... args) {
		super(code, args);
	}

	public AbstractCoreException(String message, int code, Object... args) {
		super(message, code, args);
	}

	public AbstractCoreException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}

	public AbstractCoreException(Throwable cause) {
		super(cause);
	}

}
