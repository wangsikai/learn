package com.lanking.uxb.service.cache.api.impl;

import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.cache.api.CacheService;
import com.lanking.uxb.service.cache.api.RedisSerializerProvider;
import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.config.RedisConfig;
import com.lanking.uxb.service.cache.ex.CacheException;

@MappedSuperclass
public abstract class AbstractCacheService implements CacheService, ApplicationContextAware, InitializingBean,
		SmartLifecycle {
	private Logger logger = LoggerFactory.getLogger(AbstractCacheService.class);

	static final String REDIS_KEY_SEPARATOR = ":";
	// 默认的database
	private static final int DEFAULT_DATABASE = 0;

	@Autowired
	private RedisConfig redisConfig;

	static Map<Integer, JedisConnectionFactory> factories = Maps.newHashMap();

	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;

	public String assemblyKey(Object... ps) {
		StringBuilder keySb = new StringBuilder();
		boolean first = true;
		for (Object p : ps) {
			if (p != null) {
				if (first) {
					keySb.append(p.toString());
					first = false;
				} else {
					keySb.append(REDIS_KEY_SEPARATOR).append(p.toString());
				}
			}
		}
		return keySb.toString();
	}

	/**
	 * 缓存空间(不可重复,最好是1-3个字符,不要太长)
	 * 
	 * @return ns
	 */
	public abstract String getNs();

	/**
	 * 中文缓存空间名称
	 * 
	 * @return ns CN
	 */
	public abstract String getNsCn();

	public int getDatabase() {
		return DEFAULT_DATABASE;
	}

	public SerializerType getSerializerType() {
		return null;
	}

	public String getServiceNs() {
		throw new NotImplementedException();
	}

	private ApplicationContext appContext;

	@SuppressWarnings("rawtypes")
	private static Map<String, RedisSerializerProvider> serializerProviders = Maps.newHashMap();

	private static Map<String, String> nsMap = Maps.newHashMap();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterPropertiesSet() {
		String ns = getNs();
		if (StringUtils.isNotBlank(ns)) {
			if (nsMap.containsKey(ns)) {
				throw new CacheException(CacheException.CACHE_NS_REPEAT, ns, getNsCn());
			} else {
				nsMap.put(ns, getNsCn());
			}
		} else {
			ns = Env.getString("uxb.redis.ns");
		}
		for (RedisSerializerProvider serializerProvider : appContext.getBeansOfType(RedisSerializerProvider.class)
				.values()) {
			serializerProviders.put(serializerProvider.getType().getValue(), serializerProvider);
		}
		RedisTemplate redisTemplate = new RedisTemplate(ns);
		JedisConnectionFactory jf = factories.get(getDatabase());
		if (jf == null) {
			jf = redisConfig.jedisConnectionFactory(getDatabase());
			factories.put(getDatabase(), jf);
		}
		redisTemplate.setConnectionFactory(jf);
		redisTemplate.setDefaultSerializer(getSerializerType() == null ? serializerProviders.get(Env
				.getString("uxb.redis.serializer")) : serializerProviders.get(getSerializerType().getValue()));
		redisTemplate.afterPropertiesSet();
		setRedisTemplate(redisTemplate);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	@SuppressWarnings("rawtypes")
	private void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	private static boolean running = false;

	@Override
	public void start() {
		running = true;
		logger.debug("cache namespace:");
		for (String key : nsMap.keySet()) {
			logger.debug("{}::::::::::::::::::::{}", key, nsMap.get(key));
		}
		logger.info("cache namespace:\n{}", JSON.toJSONString(nsMap, true));
	}

	@Override
	public void stop() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

}
