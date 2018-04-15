package com.lanking.cloud.job.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@Configuration
@EnableConfigurationProperties(RegistryCenterConfigProperties.class)
@ImportResource(locations = { "${yoo-cloud.job.config.locations}" })
public class RegistryCenterAutoConfiguration {

	@Autowired
	private RegistryCenterConfigProperties registryCenterConfigProperties;

	@Bean(initMethod = "init", name = "registryCenter")
	@Qualifier("registryCenter")
	public ZookeeperRegistryCenter registryCenter() {
		return new ZookeeperRegistryCenter(new ZookeeperConfiguration(registryCenterConfigProperties.getServerList(),
				registryCenterConfigProperties.getNamespace()));
	}

	@Bean(name = "jobOperateAPI")
	public JobOperateAPI jobOperateAPI(@Qualifier("registryCenter") CoordinatorRegistryCenter regCenter) {
		JobOperateAPI jobOperateAPI = new JobOperateAPIImpl(regCenter);
		return jobOperateAPI;
	}
}
