package com.lanking.cloud.sdk.data;

import java.util.Collection;

/**
 * 如果size属性为ALL,返回所有记录 如果size属性为NONE,只返回总数 如果size小于0,忽略获取总数
 * <p/>
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 */
public interface Pageable {

	int SIZE_ALL = Integer.MAX_VALUE;
	int SIZE_NONE = 0;
	int SIZE_DEFAULT = 20;

	int OFFSET_FIRST = 0;
	int INDEX_FIRST = 1;

	int getSize();

	int getOffset();

	int getIndex();

	Collection<Order> getOrders();

	Pageable next();

	Pageable current();

	Pageable previous();
}
