package com.lanking.cloud.component.mq.consumer.strategy.impl;

import java.util.Collection;

import com.lanking.cloud.component.mq.consumer.strategy.MqConsumerShardingStrategy;

public class QueueHashCodeModShardingStrategy implements MqConsumerShardingStrategy {

	@Override
	public int sharding(Collection<Integer> instances, String queue) {
		return Math.abs(queue.hashCode()) % instances.size();
	}

}
