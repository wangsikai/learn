package com.lanking.uxb.service.code.api;

public interface BaseDataHandle {

	BaseDataType getType();

	void init();

	void reload();

	/**
	 * 计算缓存大小(字节数)
	 * 
	 * @return 所有缓存对象的字节码
	 */
	long size();

}
