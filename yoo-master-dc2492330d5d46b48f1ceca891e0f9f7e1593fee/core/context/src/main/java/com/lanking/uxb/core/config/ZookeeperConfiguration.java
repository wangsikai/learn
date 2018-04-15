package com.lanking.uxb.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.lanking.cloud.sdk.zookeeper.ZooKeeperFactoryBean;

@Configuration
public class ZookeeperConfiguration {

	@Value("${yoo-cloud.zookeeper.connectString}")
	private String connectString;

	@Value("${yoo-cloud.zookeeper.sessionTimeout}")
	private int sessionTimeout;

	@Primary
	@Bean("zooKeeper")
	@Qualifier("zooKeeper")
	ZooKeeperFactoryBean zooKeeperFactoryBean() {
		ZooKeeperFactoryBean zooKeeperFactoryBean = new ZooKeeperFactoryBean(connectString, sessionTimeout);
		return zooKeeperFactoryBean;
	}
}