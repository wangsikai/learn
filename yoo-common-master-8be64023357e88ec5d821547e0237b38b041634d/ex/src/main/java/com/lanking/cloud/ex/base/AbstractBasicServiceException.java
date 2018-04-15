package com.lanking.cloud.ex.base;

import com.lanking.cloud.ex.AbstractException;

/**
 * 基础服务抽象类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月23日
 */
public abstract class AbstractBasicServiceException extends AbstractException {

	private static final long serialVersionUID = -2287411993178592573L;

	public AbstractBasicServiceException() {
		super();
	}

	public AbstractBasicServiceException(String message) {
		super(message);
	}

	public AbstractBasicServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbstractBasicServiceException(int code, Object... args) {
		super(code, args);
	}

	public AbstractBasicServiceException(String message, int code, Object... args) {
		super(message, code, args);
	}

	public AbstractBasicServiceException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}

	public AbstractBasicServiceException(Throwable cause) {
		super(cause);
	}

}
