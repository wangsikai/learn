package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class APIAccessRateLimitException extends AbstractCoreException {

	private static final long serialVersionUID = -8494623052056707251L;

	public APIAccessRateLimitException() {
		super(API_ACCESS_RATE_LIMIT_EX);
	}
}
