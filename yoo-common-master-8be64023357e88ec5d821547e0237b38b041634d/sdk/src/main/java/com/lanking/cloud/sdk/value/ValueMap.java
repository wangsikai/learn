package com.lanking.cloud.sdk.value;

import java.util.HashMap;
import java.util.Map;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * map数据结构
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月18日
 */
public final class ValueMap extends HashMap<String, Object> {

	private static final long serialVersionUID = -8834425183592882484L;

	private ValueMap(Map<String, ?> m) {
		super(m);
	}

	private ValueMap() {
	}

	public ValueMap put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public ValueMap putIf(String key, Object value) {
		if (value != null) {
			super.put(key, value);
		}
		return this;
	}

	public ValueMap putIfNotEmpty(String key, String value) {
		if (StringUtils.isNotEmpty(value)) {
			super.put(key, value);
		}
		return this;
	}

	public ValueMap putIfNotBlank(String key, String value) {
		if (StringUtils.isNotBlank(value)) {
			super.put(key, value);
		}
		return this;
	}

	public static ValueMap value(Map<String, ?> map) {
		return new ValueMap(map);
	}

	public static ValueMap value(String key, Object value) {
		return new ValueMap().put(key, value);
	}

	public static ValueMap value(Object... pairs) {
		ValueMap param = new ValueMap();
		if (pairs != null) {
			int len = pairs.length;
			if (len % 2 != 0) {
				throw new IllegalArgumentException("Pair size not even");
			}
			for (int i = 0; i < len; i += 2) {
				Object key = pairs[i];
				if (key instanceof String) {
					param.put((String) key, pairs[i + 1]);
				} else {
					throw new IllegalArgumentException("Key not be string");
				}
			}
		}
		return param;
	}

}
