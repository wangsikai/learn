package com.lanking.cloud.sdk.bean;

import java.util.Map;

/**
 * 对象组装器泛型接口
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月13日
 * @param <T>
 */
public interface Assembler<T> {

	boolean accept(Class<?> acceptClass, Map<String, Object> hints);

	void assemble(T bean);

	void massemble(Iterable<T> beans);
}
