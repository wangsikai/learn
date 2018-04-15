package com.lanking.cloud.sdk.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

public class ZooKeeperFactoryBean implements FactoryBean<ZooKeeper> {

	private Logger logger = LoggerFactory.getLogger(ZooKeeperFactoryBean.class);

	private ZooKeeper zooKeeper;

	private String connectString;
	private int sessionTimeout;

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public ZooKeeperFactoryBean() {
		super();
	}

	public ZooKeeperFactoryBean(String connectString, int sessionTimeout) {
		super();
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
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

	@Override
	public ZooKeeper getObject() throws Exception {
		return zooKeeper;
	}

	@Override
	public Class<?> getObjectType() {
		return ZooKeeper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}