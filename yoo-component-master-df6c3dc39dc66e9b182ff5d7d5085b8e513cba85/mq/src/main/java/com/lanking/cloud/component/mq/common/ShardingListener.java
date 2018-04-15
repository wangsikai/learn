package com.lanking.cloud.component.mq.common;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.mq.common.strategy.MqConsumerShardingStrategyFactory;
import com.lanking.cloud.component.mq.config.RabbitMqConsumerShardingConfigProperties;
import com.lanking.cloud.component.mq.consumer.strategy.MqConsumerShardingStrategy;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.value.ValueMap;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShardingListener implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {
	private Converter<byte[], Object> deserializer = new DeserializingConverter();

	@Autowired
	@Qualifier("rabbitMqRevExecutor")
	private Executor taskExecutor;
	@Autowired
	@Qualifier("rabbitMqConsumerShardingCuratorFramework")
	private CuratorFramework client;
	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private RabbitMqConsumerShardingConfigProperties rabbitMqConsumerShardingConfigProperties;
	@Autowired
	private MetadataAssembler metadataAssembler;
	@Autowired
	private MetadataDeclarer metadataDeclarer;

	private static volatile int currentInstance;
	private static TreeCache currentInstanceTreeCache;
	private static long currentInstanceCreatedTime;
	private static volatile Set<Integer> instances = Sets.newConcurrentHashSet();
	private Map<String, SimpleMessageListenerContainer> containers = Maps.newHashMap();
	private static volatile boolean allContainerStart = true;
	private RabbitOperations rabbit = null;
	private MqConsumerShardingStrategy consumerShardingStrategy;

	private void registerInstance() {
		InterProcessMutex lock = null;
		try {
			String lockPath = rabbitMqConsumerShardingConfigProperties.getShardingLockPath();
			if (client.checkExists().forPath(lockPath) == null) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(lockPath);
			}
			lock = new InterProcessMutex(client, lockPath);
			if (!lock.acquire(rabbitMqConsumerShardingConfigProperties.getZkLockWaitTimeMs(), TimeUnit.MILLISECONDS)) {
				throw new TimeoutException("acquire lock failed...");
			}

			String instancesPath = rabbitMqConsumerShardingConfigProperties.getShardingInstancesPath();
			if (client.checkExists().forPath(instancesPath) == null) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(instancesPath);
			}
			Set<Integer> zkInstances = new LinkedHashSet<>(
					Lists.transform(client.getChildren().forPath(instancesPath), new Function<String, Integer>() {
						@Override
						public Integer apply(final String instance) {
							return Integer.parseInt(instance);
						}
					}));
			String currentInstancePath = null;
			for (int i = 0; i < rabbitMqConsumerShardingConfigProperties.getMaxShardingInstances(); i++) {
				if (!zkInstances.contains(i)) {
					currentInstance = i;
					instances.clear();
					instances.addAll(zkInstances);
					instances.add(i);

					currentInstancePath = Joiner.on("/").join(instancesPath, i);
					String currentInstanceData = String.format("{\"ip\":\"%s\",\"hostName\":\"%s\",\"pid\":\"%s\"}",
							InetAddress.getLocalHost().getHostAddress(), InetAddress.getLocalHost().getHostName(),
							ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
					client.inTransaction().create().withMode(CreateMode.EPHEMERAL).forPath(currentInstancePath).and()
							.setData().forPath(currentInstancePath, currentInstanceData.getBytes()).and().commit();
					break;
				}
			}

			currentInstanceCreatedTime = client.checkExists().forPath(currentInstancePath).getCtime();
			currentInstanceTreeCache = new TreeCache(client, currentInstancePath);
			final String $currentInstancePath = currentInstancePath;
			currentInstanceTreeCache.getListenable().addListener(new TreeCacheListener() {
				@Override
				public void childEvent(final CuratorFramework curatorFramework, final TreeCacheEvent treeCacheEvent)
						throws Exception {
					long pathTime;
					try {
						pathTime = curatorFramework.checkExists().forPath($currentInstancePath).getCtime();
					} catch (final Exception e) {
						pathTime = 0;
					}
					if (currentInstanceCreatedTime != pathTime) {
						registerInstance();
						currentInstanceTreeCache.close();
						currentInstanceTreeCache = null;
					}
				}
			});
			currentInstanceTreeCache.start();
		} catch (Exception e) {
			log.error("register instance error:", e);
		} finally {
			try {
				lock.release();
			} catch (Exception ignored) {
			}
		}
	}

	private void consume(boolean first) {
		allContainerStart = true;
		if (!first) {
			List<String> removeQueue = Lists.newArrayList();
			for (SimpleMessageListenerContainer container : containers.values()) {
				String queue = container.getQueueNames()[0];
				int mode = Math.abs(queue.hashCode()) % instances.size();
				if (currentInstance != mode) {
					removeQueue.add(queue);
					container.stop();
				}
			}
			for (String queue : removeQueue) {
				containers.remove(queue);
			}
		}
		Executor executor = new SimpleAsyncTaskExecutor("rabbitmq-listener");
		for (Listener listener : metadataAssembler.getListeners()) {
			if (!listener.isConsume()) {
				continue;
			}
			String exchangeType = metadataAssembler.getExchanges()
					.get(metadataAssembler.getBindings().get(listener.getQueue()).getExchange()).getType();
			if (exchangeType.equals(ExchangeTypes.DIRECT) && listener.isSeries()
					&& !containers.containsKey(listener.getQueue())) {
				try {
					if (currentInstance == consumerShardingStrategy.sharding(instances, listener.getQueue())) {
						rabbit.execute(new ChannelCallback<Object>() {
							@Override
							public Object doInRabbit(Channel channel) throws Exception {
								long consumerCount = channel.consumerCount(listener.getQueue());
								if (consumerCount == 0) {
									containers.put(listener.getQueue(), consumeOne(listener, executor));
								} else {
									if (allContainerStart) {
										allContainerStart = false;
									}
								}
								return null;

							}
						});
					}
				} catch (Exception e) {
					if (allContainerStart) {
						allContainerStart = false;
					}
					log.warn("{} container start fail!!!", listener.getQueue(), e);
				}
			} else if (first && (exchangeType.equals(ExchangeTypes.FANOUT) || !listener.isSeries())) {
				consumeOne(listener, executor);
			}
		}
		try {
			Map<String, Object> data = new HashMap<>(4);
			data.put("ip", InetAddress.getLocalHost().getHostAddress());
			data.put("host", InetAddress.getLocalHost().getHostName());
			data.put("process", ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
			data.put("queues", containers.keySet());
			client.setData().forPath(Joiner.on("/")
					.join(rabbitMqConsumerShardingConfigProperties.getShardingInstancesPath(), currentInstance),
					JSON.toJSONString(data).getBytes());
		} catch (Exception ignored) {
			log.warn("set current instance data error:", ignored);
		}
	}

	private SimpleMessageListenerContainer consumeOne(Listener listener, Executor executor) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer() {
			@Override
			protected void invokeListener(final Channel channel, final Message message) throws Exception {
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						handleMessage(listener.getHandler(), listener.getMethod(), listener.isAutoAck(),
								listener.isRequeue(), message, channel);
					}
				});

			}
		};
		container.setConnectionFactory(connectionFactory);
		container.setPrefetchCount(listener.getPrefetchCount());
		container.setAcknowledgeMode(listener.isAutoAck() ? AcknowledgeMode.AUTO : AcknowledgeMode.MANUAL);
		container.setDefaultRequeueRejected(listener.isRequeue());
		container.setTaskExecutor(executor);
		container.setQueueNames(listener.getQueue());
		container.setExclusive(listener.isExclusive());
		container.setConsumerArguments(ValueMap.value("instance", currentInstance).put("series", listener.isSeries()));
		container.start();
		return container;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		registerInstance();
		metadataDeclarer.declare();
		rabbit = new RabbitTemplate(connectionFactory);
		consumerShardingStrategy = MqConsumerShardingStrategyFactory
				.getStrategy(rabbitMqConsumerShardingConfigProperties.getConsumerShardingStrategy());
		consume(true);

		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(1);
		scheduler.afterPropertiesSet();
		scheduler.scheduleWithFixedDelay(new InstanceMonitor(), 1000);
	}

	@Override
	public void destroy() throws Exception {
		for (SimpleMessageListenerContainer container : containers.values()) {
			try {
				container.stop();
				log.info("container stop success,queue:{}", container.getQueueNames()[0]);
			} catch (Exception e) {
				log.error("stop container:", e);
			}
		}
	}

	class InstanceMonitor implements Runnable {
		@Override
		public void run() {
			log.debug("current instance:{}", currentInstance);
			log.debug("all container start:{}", allContainerStart);
			log.debug("instances:{}", JSONObject.toJSONString(instances));
			if (allContainerStart) {
				InterProcessMutex lock = null;
				try {
					String lockPath = rabbitMqConsumerShardingConfigProperties.getShardingLockPath();
					if (client.checkExists().forPath(lockPath) == null) {
						client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(lockPath);
					}
					lock = new InterProcessMutex(client, lockPath);
					if (!lock.acquire(rabbitMqConsumerShardingConfigProperties.getZkLockWaitTimeMs(),
							TimeUnit.MILLISECONDS)) {
						throw new TimeoutException("acquire lock failed...");
					}

					String instancesPath = rabbitMqConsumerShardingConfigProperties.getShardingInstancesPath();
					if (client.checkExists().forPath(instancesPath) == null) {
						client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
								.forPath(instancesPath);
					}
					Set<Integer> zkInstances = new LinkedHashSet<>(Lists
							.transform(client.getChildren().forPath(instancesPath), new Function<String, Integer>() {
								@Override
								public Integer apply(final String instance) {
									return Integer.parseInt(instance);
								}
							}));
					boolean change = zkInstances.size() != instances.size();
					if (!change) {
						for (Integer zkInstance : zkInstances) {
							if (!instances.contains(zkInstance)) {
								change = true;
								break;
							}
						}
					}
					if (change) {
						log.info("instances changes,old:{},new:{}", JSON.toJSONString(instances),
								JSON.toJSONString(zkInstances));
						instances.clear();
						instances.addAll(zkInstances);
						consume(false);
					}
				} catch (Exception e) {
					log.error("instance monitor error:", e);
				} finally {
					try {
						lock.release();
					} catch (Exception ignored) {
					}
				}
			} else {
				consume(false);
			}
		}
	}

	private void handleMessage(Object handler, Method method, boolean autoAck, boolean requeue, Message message,
			Channel channel) {
		try {
			Type[] types = method.getGenericParameterTypes();
			Object[] args = new Object[types.length];
			for (int i = 0; i < types.length; i++) {
				Type type = types[i];
				if (type == Message.class) {
					args[i] = message;
				} else if (type == Channel.class) {
					args[i] = channel;
				} else if (type == String.class) {
					args[i] = Codecs.toString(message.getBody());
				} else {
					String contentType = message.getMessageProperties().getContentType();
					if (MessageProperties.CONTENT_TYPE_JSON.equals(contentType)) {
						args[i] = JSON.parseObject(message.getBody(), type);
					} else {
						args[i] = deserializer.convert(message.getBody());
					}
				}
			}
			method.invoke(handler, args);
			if (!autoAck) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		} catch (Throwable e) {
			if (!autoAck) {
				try {
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, requeue);
				} catch (IOException ignored) {
				}
			}
			if (e instanceof InvocationTargetException) {
				e = e.getCause();
			}
		}
	}
}
