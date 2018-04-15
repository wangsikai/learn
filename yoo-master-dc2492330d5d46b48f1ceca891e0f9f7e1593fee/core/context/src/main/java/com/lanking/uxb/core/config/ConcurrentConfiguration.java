package com.lanking.uxb.core.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ConcurrentConfiguration {

	@Value("${core.executor.corePoolSize}")
	private int corePoolSize;

	@Value("${core.executor.maxPoolSize}")
	private int maxPoolSize;

	@Value("${core.executor.namePrefix}")
	private String namePrefix;

	@Value("${core.executor.queueCapacity}")
	private int queueCapacity;

	@Bean("executor")
	@Qualifier("executor")
	Executor executor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setThreadNamePrefix(namePrefix);
		taskExecutor.setQueueCapacity(queueCapacity);
		return taskExecutor;
	}

}