package com.lanking.uxb.service;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.lanking.cloud.springboot.MicroServiceApplication;
import com.lanking.uxb.core.ApplicationConfiguration;

@EnableDiscoveryClient
@EnableEurekaClient
public class PackApplication {

	public static void main(String[] args) {
		// 所有服务
		new MicroServiceApplication(ApplicationConfiguration.class).run(args);
	}
}