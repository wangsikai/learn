package com.lanking.cloud.sdk.bean;

import java.util.Collection;
import java.util.Map;

/**
 * 属性接口
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月13日
 */
public interface Attrable {

	boolean hasAttr(String key);

	String getAttr(String key);

	String getAttr(String key, String defaultValue);

	boolean getBoolAttr(String key);

	byte getByteAttr(String key);

	short getShortAttr(String key);

	int getIntAttr(String key);

	long getLongAttr(String key);

	float getFloatAttr(String key);

	double getDoubleAttr(String key);

	<T> T getObject(String key, Class<T> clazz);

	Collection<String> keys();

	Map<String, String> getAttrs(String... keys);

	Map<String, String> getAttrs(Collection<String> keys);

	void setAttr(String key, Object value);

	void setAttrs(Map<String, ?> map);

	void removeAttr(String key);

	void removeAttrs(String... keys);

	void removeAttrs(Collection<String> keys);

	void clearAttrs();
}
