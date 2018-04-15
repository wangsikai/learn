package com.lanking.cloud.springboot.listener;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public final class ConfigServerListener extends AbstractConfigListener {

	public static final String UTF8 = "UTF-8".intern();

	private Logger logger = LoggerFactory.getLogger(ConfigServerListener.class);

	private ZooKeeper zooKeeper;
	private String zookeeperConnectString = null;
	private Integer zookeeperSessionTimeout = null;

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		super.onApplicationEvent(event);
		ConfigurableEnvironment env = event.getEnvironment();

		zookeeperConnectString = env.getProperty("yoo-cloud.zookeeper.connectString");
		zookeeperSessionTimeout = env.getProperty("yoo-cloud.zookeeper.sessionTimeout", Integer.class);
		initZooKeeper(zookeeperConnectString, zookeeperSessionTimeout);

		String configServerPath = env.getProperty("yoo-cloud.service.configServerPath");
		try {
			String config = load(false, configServerPath);
			Map<String, Object> map = zookeeperPropertyMap(config);
			if (event.getEnvironment().getPropertySources().contains(COMMAND_LINE_ARGS)) {
				event.getEnvironment().getPropertySources().addAfter(COMMAND_LINE_ARGS,
						new MapPropertySource(CONFIG_SERVER, map));
			} else {
				event.getEnvironment().getPropertySources().addFirst(new MapPropertySource(CONFIG_SERVER, map));
			}

		} catch (Exception e) {
			logger.error("load config server error:", e);
		}

		environment = env;
	}

	private Map<String, Object> zookeeperPropertyMap(String config) throws Exception {
		Properties properties = new Properties();
		if (config != null) {
			properties.load(new ByteArrayInputStream(config.getBytes(UTF8)));
		}
		Map<String, Object> map = property2Map(properties);
		logger.debug("load config:");
		for (Object key : map.keySet()) {
			String k = key.toString();
			Object v = map.get(k);
			logger.debug("{}:{}", k, v);
		}
		return map;
	}

	private String load(boolean reload, final String configServerPath) throws Exception {
		String config = null;
		try {
			config = new String(zooKeeper.getData(configServerPath, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					try {
						load(true, configServerPath);
					} catch (Exception e) {
						logger.error("load zookeeper config error:", e);
					}
				}
			}, new Stat()), UTF8);
		} catch (SessionExpiredException e) {
			logger.error("ZK session expired:", e);

			logger.info("try close ZK...");
			zooKeeper.close();

			logger.info("try connect ZK again...");
			initZooKeeper(zookeeperConnectString, zookeeperSessionTimeout);
			logger.info("ZK connect again OK!");

			logger.info("reload config...");
			config = new String(zooKeeper.getData(configServerPath, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					try {
						load(true, configServerPath);
					} catch (Exception e) {
						logger.error("load zookeeper config error:", e);
					}
				}
			}, new Stat()), UTF8);
			logger.info("reload config OK!");
		} catch (NoNodeException e) {
			if (!reload) {
				StringBuilder sb = new StringBuilder();
				for (String path : configServerPath.split("/")) {
					if (StringUtils.isNotBlank(path)) {
						sb.append("/").append(path);
						String pathPart = sb.toString();
						if (zooKeeper.exists(pathPart, false) == null) {
							zooKeeper.create(pathPart, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
						}
						zooKeeper.setData(pathPart, ("path=" + pathPart).getBytes(), -1);
					}
				}
			} else {
				logger.error("load zookeeper config error:", e);
			}
		}
		if (reload && config != null) {
			reloadDynamicConfigs(zookeeperPropertyMap(config));
		}
		return config;
	}

	void initZooKeeper(String connectString, int sessionTimeout) {
		try {
			zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					logger.info("ZooKeeper WatchedEvent:{}", event.toString());
				}
			});
		} catch (Exception e) {
			logger.error("create zookeeper object error:", e);
		}
	}
}
