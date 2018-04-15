package com.lanking.cloud.component.db.config;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.cloud.sdk.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(SnowflakeConfigProperties.class)
public class SnowflakeAutoConfiguration implements ApplicationContextAware {

	@Autowired
	private SnowflakeConfigProperties snowflakeConfigProperties;

	private static CuratorFramework client;
	private static TreeCache treeCache;
	private static volatile long pathCreatedTime;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SnowflakeUUID.init(snowflakeConfigProperties.getWorkID());

		if (StringUtils.isNotBlank(snowflakeConfigProperties.getZkServerList())) {
			initClient();
			try {
				register();
			} catch (Exception e) {
				log.error("register workID error:", e);
				System.exit(0);
			}
		}
	}

	void register() throws Exception {
		String workPath = snowflakeConfigProperties.getWorkPath();
		if (client.checkExists().forPath(workPath) == null) {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(workPath);
		}
		InterProcessMutex lock = new InterProcessMutex(client, workPath);
		String workIDPath = snowflakeConfigProperties.getWorkIDPath();
		try {
			if (!lock.acquire(snowflakeConfigProperties.getZkLockWaitTimeMs(), TimeUnit.MILLISECONDS)) {
				throw new TimeoutException("acquire lock failed...");
			}
			if (client.checkExists().forPath(workIDPath) == null) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(workIDPath);
			}
			Set<Integer> workIDs = new LinkedHashSet<>(
					Lists.transform(client.getChildren().forPath(workIDPath), new Function<String, Integer>() {
						@Override
						public Integer apply(final String nodeIdStr) {
							return Integer.parseInt(nodeIdStr);
						}
					}));
			String oneWorkIDPath = null;
			int workID = -1;
			for (int i = 0; i < snowflakeConfigProperties.getMaxWorkID(); i++) {
				if (!workIDs.contains(i)) {
					workID = i;
					oneWorkIDPath = workIDPath + "/" + i;
					String nodeDate = String.format("{\"ip\":\"%s\",\"hostName\":\"%s\",\"pid\":\"%s\"}",
							InetAddress.getLocalHost().getHostAddress(), InetAddress.getLocalHost().getHostName(),
							ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
					client.inTransaction().create().withMode(CreateMode.EPHEMERAL).forPath(oneWorkIDPath).and()
							.setData().forPath(oneWorkIDPath, nodeDate.getBytes()).and().commit();
					break;
				}
			}
			if (workID == -1) {
				throw new Exception();
			} else {
				SnowflakeUUID.init(workID);
			}
			pathCreatedTime = client.checkExists().forPath(oneWorkIDPath).getCtime();
			treeCache = new TreeCache(client, oneWorkIDPath);
			final String $oneWorkIDPath = oneWorkIDPath;
			treeCache.getListenable().addListener(new TreeCacheListener() {
				@Override
				public void childEvent(final CuratorFramework curatorFramework, final TreeCacheEvent treeCacheEvent)
						throws Exception {
					long pathTime;
					try {
						pathTime = curatorFramework.checkExists().forPath($oneWorkIDPath).getCtime();
					} catch (final Exception e) {
						pathTime = 0;
					}
					if (pathCreatedTime != pathTime) {
						register();
						treeCache.close();
						treeCache = null;
					}
				}
			});
			treeCache.start();
		} catch (final Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			try {
				lock.release();
			} catch (final IllegalMonitorStateException ignored) {
			}
		}
	}

	void closeClient() {
		if (client != null && client.getState() == null) {
			client.close();
		}
		client = null;
	}

	void initClient() {
		closeClient();

		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
				.connectString(snowflakeConfigProperties.getZkServerList())
				.sessionTimeoutMs(snowflakeConfigProperties.getZkSessionTimeoutMs())
				.connectionTimeoutMs(snowflakeConfigProperties.getZkConnectionTimeoutMs()).canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(snowflakeConfigProperties.getZkBaseSleepTimeMs(),
						Integer.MAX_VALUE))
				.namespace(null).defaultData(null);
		client = builder.build();
		client.start();
	}

}
