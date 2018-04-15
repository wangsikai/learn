package com.lanking.uxb.service.code.api;

/**
 * 此接口负责基础数据的更新
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
public interface BaseDataService {

	void init(BaseDataType baseDataType);

	void reload(BaseDataType baseDataType);
}
