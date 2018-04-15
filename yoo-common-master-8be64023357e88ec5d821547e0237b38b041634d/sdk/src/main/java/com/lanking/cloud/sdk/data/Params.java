package com.lanking.cloud.sdk.data;

import java.util.HashMap;
import java.util.Map;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月27日
 *
 */
public final class Params extends HashMap<String, Object> {

	private static final long serialVersionUID = -965928700490085751L;

	private Params(Map<String, ?> m) {
		super(m);
	}

	private Params() {
	}

	public Params put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public Params putIf(String key, Object value) {
		if (value != null) {
			super.put(key, value);
		}
		return this;
	}

	public Params putIfNotEmpty(String key, String value) {
		if (StringUtils.isNotEmpty(value)) {
			super.put(key, value);
		}
		return this;
	}

	public Params putIfNotBlank(String key, String value) {
		if (StringUtils.isNotBlank(value)) {
			super.put(key, value);
		}
		return this;
	}

	public static Params param(Map<String, ?> map) {
		return new Params(map);
	}

	public static Params param(String key, Object value) {
		return new Params().put(key, value);
	}

	public static Params param(Object... pairs) {
		Params param = new Params();
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
