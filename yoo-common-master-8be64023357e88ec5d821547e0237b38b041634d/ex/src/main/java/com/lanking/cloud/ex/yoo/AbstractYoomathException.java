package com.lanking.cloud.ex.yoo;

/**
 * yoomath异常基类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年12月2日
 */
public abstract class AbstractYoomathException extends AbstractYooException {

	private static final long serialVersionUID = 3505277579121719480L;

	public AbstractYoomathException(int code, Object... args) {
		super(code, args);
	}
}
