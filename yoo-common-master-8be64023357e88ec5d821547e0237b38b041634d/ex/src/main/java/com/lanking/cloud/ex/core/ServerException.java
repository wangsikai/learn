package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class ServerException extends AbstractCoreException {

	private static final long serialVersionUID = -8652101660742177453L;

	public ServerException() {
		super(SERVER_EX);
	}

	public ServerException(String message) {
		super(message, SERVER_EX);
	}

	public ServerException(Throwable cause) {
		super(SERVER_EX, cause);
	}
}
