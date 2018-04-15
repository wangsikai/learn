package com.lanking.cloud.ex.support;

import com.lanking.cloud.ex.AbstractException;

/**
 * 支撑系统异常基类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月30日
 */
public abstract class AbstractSupportSystemException extends AbstractException {

	private static final long serialVersionUID = 3059345140662467313L;

	public AbstractSupportSystemException() {
	};

	public AbstractSupportSystemException(int code, Object... args) {
		super(code, args);
	}
}
