package com.lanking.cloud.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties(prefix = "yoo-cloud.job.registryCenter")
public class RegistryCenterConfigProperties {

	private String serverList;
	private String namespace;
	private int sessionTimeoutMs = 60 * 1000;
	private int connectionTimeoutMs = 30000;
	private int baseSleepTimeMs = 1000;

}
