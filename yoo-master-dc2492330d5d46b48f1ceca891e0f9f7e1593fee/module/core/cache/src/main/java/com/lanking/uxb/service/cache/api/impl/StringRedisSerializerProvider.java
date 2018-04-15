package com.lanking.uxb.service.cache.api.impl;

import java.nio.charset.Charset;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.lanking.uxb.service.cache.api.RedisSerializerProvider;
import com.lanking.uxb.service.cache.api.SerializerType;

@Component
public class StringRedisSerializerProvider implements RedisSerializerProvider<String> {

	@Override
	public SerializerType getType() {
		return SerializerType.STRING;
	}

	private final Charset charset;

	public StringRedisSerializerProvider() {
		this(Charset.forName("UTF8"));
	}

	public StringRedisSerializerProvider(Charset charset) {
		Assert.notNull(charset);
		this.charset = charset;
	}

	public String deserialize(byte[] bytes) {
		return (bytes == null ? null : new String(bytes, charset));
	}

	public byte[] serialize(String string) {
		return (string == null ? null : string.getBytes(charset));
	}
}
