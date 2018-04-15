package com.lanking.uxb.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
@EnableFeignClients(basePackages = {"com.lanking.intercomm"})
@ComponentScan(basePackages = { "com.lanking", "com.dangdang" })
public class ApplicationConfiguration {
}
