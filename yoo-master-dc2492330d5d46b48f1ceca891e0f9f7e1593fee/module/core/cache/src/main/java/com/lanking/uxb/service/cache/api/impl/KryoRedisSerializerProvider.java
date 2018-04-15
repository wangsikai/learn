package com.lanking.uxb.service.cache.api.impl;

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lanking.cloud.sdk.util.ArrayUtils;
import com.lanking.cloud.sdk.util.Classes;
import com.lanking.uxb.service.cache.api.RedisSerializerProvider;
import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.ex.CacheException;

@Component
public class KryoRedisSerializerProvider implements RedisSerializerProvider<Object> {

	private static final Kryo KRYO;

	static {
		KRYO = new Kryo();
		KRYO.setClassLoader(Thread.currentThread().getContextClassLoader());
		KRYO.setInstantiatorStrategy(new StdInstantiatorStrategy());
		KRYO.setReferences(false);
	}

	private Class<Object> type;
	private Kryo kryo = KRYO;

	public KryoRedisSerializerProvider() {
		type = Classes.getGenericParameter0(getClass());
	}

	public KryoRedisSerializerProvider(Class<Object> type) {
		this.type = type;
	}

	public void setType(Class<Object> type) {
		this.type = type;
	}

	public void setKryo(Kryo kryo) {
		this.kryo = kryo;
	}

	@Override
	public byte[] serialize(Object obj) throws SerializationException {
		if (obj == null) {
			return null;
		}
		try {
			Output output = new Output(1024, -1);
			if (type != null) {
				kryo.writeObject(output, obj);
			} else {
				kryo.writeClassAndObject(output, obj);
			}
			return output.toBytes();
		} catch (Exception e) {
			throw new CacheException(CacheException.CACHE_SERIALIZE_ERROR);
		}

	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (ArrayUtils.isEmpty(bytes)) {
			return null;
		}
		try {
			Input input = new Input(bytes);
			if (type != null) {
				return kryo.readObject(input, type);
			} else {
				return kryo.readClassAndObject(input);
			}
		} catch (Exception e) {
			throw new CacheException(CacheException.CACHE_DESERIALIZE_ERROR);
		}
	}

	@Override
	public SerializerType getType() {
		return SerializerType.KRYO;
	}

}
