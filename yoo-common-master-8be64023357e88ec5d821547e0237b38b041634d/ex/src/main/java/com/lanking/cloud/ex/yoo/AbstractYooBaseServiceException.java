package com.lanking.cloud.ex.yoo;

/**
 * yoo系列基础公共服务异常基类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年12月2日
 */
public abstract class AbstractYooBaseServiceException extends AbstractYooException {

	private static final long serialVersionUID = 5617827309779219835L;

	public AbstractYooBaseServiceException(int code, Object... args) {
		super(code, args);
	}

}
