package com.lanking.uxb.service.cache.api;

import org.springframework.data.redis.serializer.RedisSerializer;

public interface RedisSerializerProvider<T> extends RedisSerializer<T> {

	SerializerType getType();
}
