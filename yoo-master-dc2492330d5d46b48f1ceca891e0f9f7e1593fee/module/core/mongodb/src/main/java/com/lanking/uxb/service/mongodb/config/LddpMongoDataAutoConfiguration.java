package com.lanking.uxb.service.mongodb.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.lanking.uxb.service.mongodb.api.impl.LddpMongoTemplateImpl;
import com.mongodb.Mongo;

/**
 * 出于当前平台sprint-boot使用的mongo版本比较低，所以放弃使用集成的AutoConfiguration
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月23日
 */
@Configuration
@ConditionalOnClass({ Mongo.class, MongoTemplate.class })
// @ConditionalOnBean(LddpMongoProperties.class)
@AutoConfigureAfter(LddpMongoAutoConfiguration.class)
public class LddpMongoDataAutoConfiguration {

	@Autowired
	private LddpMongoProperties properties;

	@SuppressWarnings("deprecation")
	@Bean
	@ConditionalOnMissingBean
	public MongoDbFactory mongoDbFactory(Mongo mongo) throws Exception {
		return new SimpleMongoDbFactory(mongo, properties.getDatabase());
	}

	@Bean
	@ConditionalOnMissingBean
	public LddpMongoTemplateImpl mongoTemplate(MongoDbFactory mongoDbFactory) throws UnknownHostException {
		return new LddpMongoTemplateImpl(mongoDbFactory);
	}

}
