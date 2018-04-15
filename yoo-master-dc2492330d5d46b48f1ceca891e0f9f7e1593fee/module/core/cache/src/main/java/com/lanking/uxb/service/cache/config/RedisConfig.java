package com.lanking.uxb.service.cache.config;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPoolConfig;

import com.google.common.collect.Maps;
import com.lanking.uxb.service.cache.api.RedisSerializerProvider;
import com.lanking.uxb.service.cache.api.impl.JedisConnectionFactory;

@ConditionalOnExpression("${uxb.redis.enable}")
@Configuration
@Component
public class RedisConfig implements ApplicationContextAware, InitializingBean {

	@Value("${uxb.redis.host}")
	private String host;
	@Value("${uxb.redis.port}")
	private int port;
	@Value("${uxb.redis.timeout}")
	private int timeout;
	@Value("${uxb.redis.userPool}")
	private boolean usePool;
	@Value("${uxb.redis.maxIdle}")
	private int maxIdle;
	@Value("${uxb.redis.minIdle}")
	private int minIdle;
	@Value("${uxb.redis.maxActive}")
	private int maxActive;
	@Value("${uxb.redis.maxWait}")
	private int maxWait;
	@Value("${uxb.redis.databases}")
	private int databases;

	private ApplicationContext appContext;

	@SuppressWarnings("rawtypes")
	private Map<String, RedisSerializerProvider> serializerProviders = Maps.newHashMap();

	public int getDatabases() {
		return databases;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterPropertiesSet() {
		for (RedisSerializerProvider serializerProvider : appContext.getBeansOfType(RedisSerializerProvider.class)
				.values()) {
			serializerProviders.put(serializerProvider.getType().getValue(), serializerProvider);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	public JedisConnectionFactory jedisConnectionFactory(int database) {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(host);
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setTimeout(timeout);
		jedisConnectionFactory.setUsePool(usePool);
		jedisConnectionFactory.setDatabase(database);
		if (usePool) {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(maxIdle);
			poolConfig.setMinIdle(minIdle);
			poolConfig.setMaxTotal(maxActive);
			poolConfig.setMaxWaitMillis(maxWait);
			jedisConnectionFactory.setPoolConfig(poolConfig);
		}
		jedisConnectionFactory.afterPropertiesSet();
		return jedisConnectionFactory;
	}

}
