package com.lanking.uxb.service.cache.api.impl;

import com.lanking.cloud.sdk.util.StringUtils;

public class RedisTemplate<K, V> extends org.springframework.data.redis.core.RedisTemplate<K, V> {

	private String ns;

	public RedisTemplate() {
		super();
	}

	public RedisTemplate(String ns) {
		super();
		this.ns = ns;
		if (StringUtils.isNotBlank(ns)) {
			this.setKeySerializer(new KeyRedisSerializer(ns));
		}
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (StringUtils.isNotBlank(ns)) {
			this.setKeySerializer(new KeyRedisSerializer(ns));
		}
	}

}
