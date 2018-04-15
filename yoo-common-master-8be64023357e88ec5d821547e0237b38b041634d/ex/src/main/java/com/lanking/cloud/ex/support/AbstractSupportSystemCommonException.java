package com.lanking.cloud.ex.support;

/**
 * 支撑系统公共异常基类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月30日
 */
public abstract class AbstractSupportSystemCommonException extends AbstractSupportSystemException {

	private static final long serialVersionUID = -1686456303820518354L;

	public AbstractSupportSystemCommonException(int code, Object[] args) {
		super(code, args);
	}
}
