package com.lanking.uxb.service.cache.api;

import com.lanking.uxb.service.cache.api.impl.RedisTemplate;

public interface CacheService {

	@SuppressWarnings("rawtypes")
	RedisTemplate getRedisTemplate();
}
