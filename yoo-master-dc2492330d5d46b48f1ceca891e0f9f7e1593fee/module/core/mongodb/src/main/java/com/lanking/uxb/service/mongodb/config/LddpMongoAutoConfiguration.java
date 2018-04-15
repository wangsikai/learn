package com.lanking.uxb.service.mongodb.config;

import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;

/**
 * 出于当前平台sprint-boot使用的mongo版本比较低，所以放弃使用集成的AutoConfiguration
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月23日
 */
@Configuration
@ConditionalOnClass(Mongo.class)
@EnableConfigurationProperties(LddpMongoProperties.class)
public class LddpMongoAutoConfiguration {

	@Autowired
	private LddpMongoProperties properties;

	@Autowired(required = false)
	private MongoClientOptions options;

	private Mongo mongo;

	@PreDestroy
	public void close() {
		if (this.mongo != null) {
			this.mongo.close();
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public Mongo mongo() throws UnknownHostException {
		this.mongo = this.properties.createMongoClient(options);
		return this.mongo;
	}

}
