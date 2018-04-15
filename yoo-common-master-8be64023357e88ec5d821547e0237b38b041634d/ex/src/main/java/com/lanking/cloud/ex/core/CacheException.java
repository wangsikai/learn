package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class CacheException extends AbstractCoreException {

	private static final long serialVersionUID = 1100703136000309129L;

	public CacheException() {
		super(CACHE_EX);
	}
}
