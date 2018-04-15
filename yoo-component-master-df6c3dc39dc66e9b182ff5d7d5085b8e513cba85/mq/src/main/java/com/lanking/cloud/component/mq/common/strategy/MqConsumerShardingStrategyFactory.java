package com.lanking.cloud.component.mq.common.strategy;

import com.lanking.cloud.component.mq.common.ex.MqException;
import com.lanking.cloud.component.mq.consumer.strategy.MqConsumerShardingStrategy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MqConsumerShardingStrategyFactory {

	public static MqConsumerShardingStrategy getStrategy(final String mqConsumerShardingStrategyClassName) {
		try {
			Class<?> mqConsumerShardingStrategyClass = Class.forName(mqConsumerShardingStrategyClassName);
			if (!MqShardingStrategy.class.isAssignableFrom(mqConsumerShardingStrategyClass)) {
				throw new MqException("Class " + mqConsumerShardingStrategyClassName + " is not mq strategy class");
			}
			return (MqConsumerShardingStrategy) mqConsumerShardingStrategyClass.newInstance();
		} catch (final ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			throw new MqException(
					"Class " + mqConsumerShardingStrategyClassName + " config error, message details are '%s'", ex);
		}
	}
}
