package com.lanking.cloud.component.db.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "yoo-cloud.database.snowflake")
public class SnowflakeConfigProperties {
	private int workID;
	private String workPath;
	private String workIDPath;
	private int maxWorkID = 1024;

	private String zkServerList;
	private int zkSessionTimeoutMs = 60 * 1000;
	private int zkConnectionTimeoutMs = 30000;
	private int zkBaseSleepTimeMs = 1000;
	private int zkLockWaitTimeMs = 30000;

}
