package com.lanking.cloud.component.mq.common;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.lanking.cloud.component.mq.config.RabbitMqConsumerShardingConfigProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MetadataDeclarer {

	@Autowired
	@Qualifier("rabbitMqConsumerShardingCuratorFramework")
	private CuratorFramework client;
	@Autowired
	private AmqpAdmin amqpAdmin;
	@Autowired
	private RabbitMqConsumerShardingConfigProperties rabbitMqConsumerShardingConfigProperties;
	@Autowired
	private MetadataAssembler metadataAssembler;

	public void declare() {
		InterProcessMutex lock = null;
		try {
			String lockPath = rabbitMqConsumerShardingConfigProperties.getDeclareLockPath();
			if (client.checkExists().forPath(lockPath) == null) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(lockPath);
			}
			lock = new InterProcessMutex(client, lockPath);
			if (!lock.acquire(rabbitMqConsumerShardingConfigProperties.getZkLockWaitTimeMs(), TimeUnit.MILLISECONDS)) {
				throw new TimeoutException("acquire lock failed...");
			}
			// exchange
			String directExchangesPath = rabbitMqConsumerShardingConfigProperties.getDeclareDirectExchangesPath();
			if (client.checkExists().forPath(directExchangesPath) != null) {
				client.delete().deletingChildrenIfNeeded().forPath(directExchangesPath);
			}
			if (client.checkExists().forPath(directExchangesPath) == null) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(directExchangesPath);
			}
			String fanoutExchangesPath = rabbitMqConsumerShardingConfigProperties.getDeclareFanoutExchangesPath();
			if (client.checkExists().forPath(fanoutExchangesPath) == null) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(fanoutExchangesPath);
			}

			for (Exchange exchange : metadataAssembler.getExchanges().values()) {
				amqpAdmin.declareExchange(exchange);

				if (exchange.getType().equals(ExchangeTypes.DIRECT)) {
					String directExchangePath = Joiner.on("/").join(directExchangesPath, exchange.getName());
					client.inTransaction().create().withMode(CreateMode.PERSISTENT).forPath(directExchangePath).and()
							.setData().forPath(directExchangePath, JSON.toJSONString(exchange).getBytes()).and()
							.commit();
				}
			}

			// queue
			for (Queue queue : metadataAssembler.getQueues().values()) {
				amqpAdmin.declareQueue(queue);
			}
			// binding
			for (Binding binding : metadataAssembler.getBindings().values()) {
				amqpAdmin.declareBinding(binding);

				Exchange exchange = metadataAssembler.getExchanges().get(binding.getExchange());
				if (exchange.getType().equals(ExchangeTypes.DIRECT)) {
					String directExchangeBindingPath = Joiner.on("/").join(
							Joiner.on("/").join(directExchangesPath, exchange.getName()), binding.getDestination());
					client.inTransaction().create().withMode(CreateMode.PERSISTENT).forPath(directExchangeBindingPath)
							.and().setData()
							.forPath(directExchangeBindingPath,
									JSON.toJSONString(metadataAssembler.getQueues().get(binding.getDestination()))
											.getBytes())
							.and().commit();
				} else if (exchange.getType().equals(ExchangeTypes.FANOUT)) {
					String fanoutExchangeBindingPath = Joiner.on("/").join(fanoutExchangesPath,
							Joiner.on("@").join(exchange.getName(), binding.getDestination()));
					client.create().withMode(CreateMode.EPHEMERAL).forPath(fanoutExchangeBindingPath);
				}

			}
		} catch (Exception e) {
			log.error("declare error:", e);
		} finally {
			try {
				lock.release();
			} catch (Exception ignored) {
			}
		}
	}
}
