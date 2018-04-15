package com.lanking.uxb.service.cache.api.impl;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;

public class KeyRedisSerializer implements RedisSerializer<Object> {

	private String ns;

	public KeyRedisSerializer(String ns) {
		super();
		if (StringUtils.isNotBlank(ns)) {
			if (StringUtils.isBlank(Env.getString("uxb.redis.ns.prefix"))) {
				this.ns = ns + AbstractCacheService.REDIS_KEY_SEPARATOR;
			} else {
				this.ns = Env.getString("uxb.redis.ns.prefix") + AbstractCacheService.REDIS_KEY_SEPARATOR + ns
						+ AbstractCacheService.REDIS_KEY_SEPARATOR;
			}
		}
	}

	public Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			String key = Codecs.toString(bytes);
			if (StringUtils.isBlank(ns)) {
				return key;
			} else {
				return key.indexOf(ns) == 0 ? key.substring(ns.length()) : key;
			}
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}

	public byte[] serialize(Object object) {
		if (object == null) {
			return new byte[0];
		}
		try {
			if (StringUtils.isBlank(ns)) {
				return Codecs.toBytes(object.toString());
			} else {
				return Codecs.toBytes(ns + object.toString());
			}
		} catch (Exception ex) {
			throw new SerializationException("Cannot serialize", ex);
		}
	}
}
