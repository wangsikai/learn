package com.lanking.cloud.component.mq.common.ex;

import com.lanking.cloud.ex.base.AbstractBasicServiceException;

public class MqException extends AbstractBasicServiceException {

	private static final long serialVersionUID = -6541055598651966772L;

	static final int MQ_ERROR = 200;

	public MqException() {
		super();
	}

	public MqException(String message) {
		super(message);
	}

	public MqException(String message, Throwable cause) {
		super(message, cause);
	}

}
