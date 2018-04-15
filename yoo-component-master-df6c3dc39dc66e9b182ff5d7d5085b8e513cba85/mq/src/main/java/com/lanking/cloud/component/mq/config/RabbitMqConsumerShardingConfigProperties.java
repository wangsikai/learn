package com.lanking.cloud.component.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "yoo-cloud.mq.rabbitmq.consumerSharding")
public class RabbitMqConsumerShardingConfigProperties {

	private String zkNodes;
	private int zkSessionTimeoutMs = 60 * 1000;
	private int zkConnectionTimeoutMs = 3000;
	private int zkBaseSleepTimeMs = 1000;
	private int zkLockWaitTimeMs = 30000;

	private String declareLockPath;
	private String declareDirectExchangesPath;
	private String declareFanoutExchangesPath;

	private String shardingLockPath;
	private String shardingInstancesPath;
	private int maxShardingInstances = 1024;

	private String consumerShardingStrategy = "com.lanking.cloud.component.mq.consumer.strategy.impl.QueueHashCodeModShardingStrategy";

}
