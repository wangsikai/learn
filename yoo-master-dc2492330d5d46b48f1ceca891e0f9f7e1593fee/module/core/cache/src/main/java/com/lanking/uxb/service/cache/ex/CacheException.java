package com.lanking.uxb.service.cache.ex;

import com.lanking.cloud.ex.base.AbstractBasicServiceException;

public class CacheException extends AbstractBasicServiceException {

	private static final long serialVersionUID = -9163649969635620235L;

	static final int CACHE_ERROR = 300;

	/**
	 * 序列化错误
	 */
	public static final int CACHE_SERIALIZE_ERROR = CACHE_ERROR + 1;

	/**
	 * 反序列化错误
	 */
	public static final int CACHE_DESERIALIZE_ERROR = CACHE_ERROR + 2;

	/**
	 * 自定义NS重复
	 */
	public static final int CACHE_NS_REPEAT = CACHE_ERROR + 3;
	/**
	 * database index OutOfBounds
	 */
	public static final int DATABASE_INDEX_OUTOFBOUNDS = CACHE_ERROR + 4;

	public CacheException(int code, Object... args) {
		super(code, args);
	}

}
