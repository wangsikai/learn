package com.lanking.uxb.service.cache.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO 为shard做准备
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月25日
 *
 */
public class JedisConnectionFactory extends org.springframework.data.redis.connection.jedis.JedisConnectionFactory {

	private Logger logger = LoggerFactory.getLogger(JedisConnectionFactory.class);

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		logger.info("redis info,host:{},port:{},timeout:{},userPool:{},database:{}", this.getHostName(),
				this.getPort(), this.getTimeout(), this.getUsePool(), this.getDatabase());
		logger.info("ping:{}", this.getConnection().ping());
	}
}
