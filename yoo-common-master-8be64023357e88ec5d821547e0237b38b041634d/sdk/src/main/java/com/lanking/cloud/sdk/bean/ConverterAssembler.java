package com.lanking.cloud.sdk.bean;

import java.util.Collection;
import java.util.Map;

/**
 * 对象转换组装器泛型接口
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月13日
 * @param <D>
 *            目标对象
 * @param <S>
 *            源对象
 * @param <K>
 *            获取值的key
 * @param <V>
 *            值
 */
public interface ConverterAssembler<D, S, K, V> {

	boolean accept(S s);

	boolean accept(Map<String, Object> hints);

	K getKey(S s, D d);

	void setValue(S s, D d, V value);

	V getValue(K key);

	Map<K, V> mgetValue(Collection<K> keys);
}
