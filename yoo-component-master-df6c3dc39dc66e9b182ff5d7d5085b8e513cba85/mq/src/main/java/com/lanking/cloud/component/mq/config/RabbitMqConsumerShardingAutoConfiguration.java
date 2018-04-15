package com.lanking.cloud.component.mq.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitMqConsumerShardingConfigProperties.class)
public class RabbitMqConsumerShardingAutoConfiguration {

	@Autowired
	private RabbitMqConsumerShardingConfigProperties rabbitMqConsumerShardingConfigProperties;

	@Bean("rabbitMqConsumerShardingCuratorFramework")
	@Qualifier("rabbitMqConsumerShardingCuratorFramework")
	CuratorFramework curatorFramework() {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
				.connectString(rabbitMqConsumerShardingConfigProperties.getZkNodes())
				.sessionTimeoutMs(rabbitMqConsumerShardingConfigProperties.getZkSessionTimeoutMs())
				.connectionTimeoutMs(rabbitMqConsumerShardingConfigProperties.getZkConnectionTimeoutMs())
				.canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(
						rabbitMqConsumerShardingConfigProperties.getZkBaseSleepTimeMs(), Integer.MAX_VALUE))
				.namespace(null).defaultData(null);
		CuratorFramework client = builder.build();
		client.start();
		return client;
	}

}
