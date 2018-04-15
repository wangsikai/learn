package com.lanking.cloud.ex.yoo;

import com.lanking.cloud.ex.AbstractException;

/**
 * yoo系列异常基类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年12月2日
 */
public abstract class AbstractYooException extends AbstractException {

	private static final long serialVersionUID = -564710878663659681L;

	public AbstractYooException(int code, Object... args) {
		super(code, args);
	}

}
