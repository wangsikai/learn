package com.lanking.cloud.component.mq.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class RabbitMqExecutorAutoConfiguration {

	@Bean("rabbitMqRevExecutorConfigProperties")
	@Qualifier("rabbitMqRevExecutorConfigProperties")
	@ConfigurationProperties("yoo-cloud.mq.rabbitmq.executor.rev")
	RabbitMqExecutorConfigProperties rabbitMqRevExecutorConfigProperties() {
		return new RabbitMqExecutorConfigProperties();
	}

	@Bean("rabbitMqRevExecutor")
	@Qualifier("rabbitMqRevExecutor")
	@ConfigurationProperties("yoo-cloud.mq.rabbitmq.executor.rev")
	Executor rabbitMqRevExecutor(
			@Qualifier("rabbitMqRevExecutorConfigProperties") RabbitMqExecutorConfigProperties rabbitMqRevExecutorConfigProperties) {
		ThreadPoolTaskExecutor rabbitMqRevExecutor = new ThreadPoolTaskExecutor();
		rabbitMqRevExecutor.setCorePoolSize(rabbitMqRevExecutorConfigProperties.getCorePoolSize());
		rabbitMqRevExecutor.setMaxPoolSize(rabbitMqRevExecutorConfigProperties.getMaxPoolSize());
		rabbitMqRevExecutor.setThreadNamePrefix(rabbitMqRevExecutorConfigProperties.getNamePrefix());
		rabbitMqRevExecutor.setQueueCapacity(rabbitMqRevExecutorConfigProperties.getQueueCapacity());
		return rabbitMqRevExecutor;
	}

	@Bean("rabbitMqSendExecutorConfigProperties")
	@Qualifier("rabbitMqSendExecutorConfigProperties")
	@ConfigurationProperties("yoo-cloud.mq.rabbitmq.executor.Send")
	RabbitMqExecutorConfigProperties rabbitMqSendExecutorConfigProperties() {
		return new RabbitMqExecutorConfigProperties();
	}

	@Bean("rabbitMqSendExecutor")
	@Qualifier("rabbitMqSendExecutor")
	@ConfigurationProperties("yoo-cloud.mq.rabbitmq.executor.Send")
	Executor rabbitMqSendExecutor(
			@Qualifier("rabbitMqSendExecutorConfigProperties") RabbitMqExecutorConfigProperties rabbitMqSendExecutorConfigProperties) {
		ThreadPoolTaskExecutor rabbitMqSendExecutor = new ThreadPoolTaskExecutor();
		rabbitMqSendExecutor.setCorePoolSize(rabbitMqSendExecutorConfigProperties.getCorePoolSize());
		rabbitMqSendExecutor.setMaxPoolSize(rabbitMqSendExecutorConfigProperties.getMaxPoolSize());
		rabbitMqSendExecutor.setThreadNamePrefix(rabbitMqSendExecutorConfigProperties.getNamePrefix());
		rabbitMqSendExecutor.setQueueCapacity(rabbitMqSendExecutorConfigProperties.getQueueCapacity());
		rabbitMqSendExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return rabbitMqSendExecutor;
	}
}
