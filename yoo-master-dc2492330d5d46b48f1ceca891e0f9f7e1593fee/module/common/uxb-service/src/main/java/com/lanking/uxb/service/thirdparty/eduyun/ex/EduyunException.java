package com.lanking.uxb.service.thirdparty.eduyun.ex;

public class EduyunException extends RuntimeException {
	private static final long serialVersionUID = 5961390544977251724L;

	private int code;
	private Object args[];

	public static final int TOKEN_OR_DIGITAL_INCORRENT = 1;

	public EduyunException() {
		super();
	}

	public EduyunException(int code, Object... args) {
		this();
		this.code = code;
		this.args = args;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return getCode() + ":" + getMessage();
	}
}
