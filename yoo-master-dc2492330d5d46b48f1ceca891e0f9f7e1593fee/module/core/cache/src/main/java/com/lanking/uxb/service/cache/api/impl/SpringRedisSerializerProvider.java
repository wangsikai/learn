package com.lanking.uxb.service.cache.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.stereotype.Component;

import com.lanking.uxb.service.cache.api.RedisSerializerProvider;
import com.lanking.uxb.service.cache.api.SerializerType;

@Component
public class SpringRedisSerializerProvider implements RedisSerializerProvider<Object> {

	private Logger logger = LoggerFactory.getLogger(SpringRedisSerializerProvider.class);

	private Converter<Object, byte[]> serializer = new SerializingConverter();
	private Converter<byte[], Object> deserializer = new DeserializingConverter();

	public Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			return deserializer.convert(bytes);
		} catch (Exception ex) {
			logger.debug("deserializer error:", ex);
			logger.info("deserializer fail:{}", ex.getMessage());
			return null;
		}
	}

	public byte[] serialize(Object object) {
		if (object == null) {
			return new byte[0];
		}
		try {
			return serializer.convert(object);
		} catch (Exception ex) {
			logger.debug("serialize error:", ex);
			logger.info("serialize fail:{}", ex.getMessage());
			return null;
		}
	}

	@Override
	public SerializerType getType() {
		return SerializerType.SPRING;
	}

}
