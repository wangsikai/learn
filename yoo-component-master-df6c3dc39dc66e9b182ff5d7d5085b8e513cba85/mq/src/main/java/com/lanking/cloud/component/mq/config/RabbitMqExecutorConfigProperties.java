package com.lanking.cloud.component.mq.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMqExecutorConfigProperties {

	private int corePoolSize;
	private int maxPoolSize;
	private String namePrefix;
	private int queueCapacity;

}
