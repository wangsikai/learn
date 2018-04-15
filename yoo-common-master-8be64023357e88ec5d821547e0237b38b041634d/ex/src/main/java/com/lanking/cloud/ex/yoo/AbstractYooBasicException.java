package com.lanking.cloud.ex.yoo;

/**
 * yoo系列公共模块异常基类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年12月2日
 */
public abstract class AbstractYooBasicException extends AbstractYooException {

	private static final long serialVersionUID = 2257470489282690520L;

	public AbstractYooBasicException(int code, Object... args) {
		super(code, args);
	}
}
