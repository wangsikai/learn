package com.lanking.uxb.operation.common.ex;

import com.lanking.cloud.ex.support.AbstractSupportSystemException;

/**
 * 运维支撑系统异常类
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年12月1日
 */
public class OperationConsoleException extends AbstractSupportSystemException {

	private static final long serialVersionUID = 4240914254712449367L;

	private static final int OPERATION_CONSOLE_ERROR = 1500000;

	/**
	 * 当前任务正在进行中，不可以修改
	 */
	public static final int OPERATION_QRTZ_TRIGGERNAME_ERROR = OPERATION_CONSOLE_ERROR + 1;

	public OperationConsoleException() {
		super();
	}

	public OperationConsoleException(int code, Object... args) {
		super(code, args);
	}

}
