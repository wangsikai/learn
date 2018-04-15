package com.lanking.uxb.service.honor.ex;

import com.lanking.cloud.ex.yoo.AbstractYooBasicException;

/**
 * 荣誉相关异常 10001 - 10050
 *
 * @author xinyu.zhou
 * @since 4.0.0
 */
public class HonorException extends AbstractYooBasicException {
	private static final long serialVersionUID = -6139991994288107489L;

	private static final int HONOR_ERROR = 10000;

	/**
	 * 任务不存在
	 */
	public static final int HONOR_USERTASK_NOT_EXISTS = HONOR_ERROR + 1;
	/**
	 * 已经领取过奖励
	 */
	public static final int HONOR_USERTASK_GETED_REWARD = HONOR_ERROR + 2;

	public HonorException(int code, Object... args) {
		super(code, args);
	}
}
