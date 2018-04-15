package com.lanking.cloud.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yoo-cloud.job.registryCenter")
public class RegistryCenterConfigProperties {

	private String serverList;
	private String namespace;

	public String getServerList() {
		return serverList;
	}

	public void setServerList(String serverList) {
		this.serverList = serverList;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
