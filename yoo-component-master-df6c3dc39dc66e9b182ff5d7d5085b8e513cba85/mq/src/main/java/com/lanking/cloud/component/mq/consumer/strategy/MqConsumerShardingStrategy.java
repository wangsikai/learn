package com.lanking.cloud.component.mq.consumer.strategy;

import java.util.Collection;

import com.lanking.cloud.component.mq.common.strategy.MqShardingStrategy;

/**
 * 消费者分片策略接口（目前只针对队列层面）<br>
 * TODO 后续考虑1.对每个queue使用不同的策略 2.队列的消息做分片
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年8月16日
 */
public interface MqConsumerShardingStrategy extends MqShardingStrategy {

	int sharding(Collection<Integer> instances, String queue);
}
