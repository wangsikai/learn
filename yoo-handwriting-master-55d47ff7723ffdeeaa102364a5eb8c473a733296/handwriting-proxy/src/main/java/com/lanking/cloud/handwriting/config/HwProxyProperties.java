package com.lanking.cloud.handwriting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "yoo-cloud.handwriting-proxy.hw")
@Configuration
public class HwProxyProperties {

	private String proxy;

}
