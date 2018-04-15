/**
 * 
 */
package com.lanking.cloud.job;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author michael
 * monitoring memory usage
 *
 */
@Component
public class InitAppUpListener implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(InitAppUpListener.class);
	private int evtCount = 0;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		evtCount++;
		ApplicationContext ac = event.getApplicationContext();
		logger.info("refreshed event count: {}", evtCount);
		logger.info("context refreshed event: id/{}, appName/{}, parent/{}", ac.getId(), ac.getApplicationName(), ac.getParent());
		logger.info("heap memory usage:{} MB", memoryUsage());
	}
	
	@Scheduled(cron = "0 * * * * ?") //every 1 minute
	public void scheduleTaskWithCronExpression() {
	    logger.info("memory usage: - {} MB", memoryUsage());
	}
	
	private long memoryUsage() {
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
		return memoryUsage.getUsed()/ 1024 / 1024;
	}
	
}