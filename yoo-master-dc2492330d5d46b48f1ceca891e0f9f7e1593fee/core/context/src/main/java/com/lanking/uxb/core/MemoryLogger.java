/**
 * 
 */
package com.lanking.uxb.core;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * @author michael
 * monitor memory usage
 *
 */
@Slf4j
@Component
public class MemoryLogger {
	
	long MB = 1024 * 1024;
	
	@Scheduled(cron = "0 * * * * ?") //every 1 minute
	public void scheduleTaskWithCronExpression() {
		memoryUsage();
	}
	
	private void memoryUsage() {
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
		
		MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
		log.info("heap memory: - {} MB, nonheap memory: {} MB", memoryUsage.getUsed()/ MB, nonHeapMemoryUsage.getUsed() /MB);
		
		// Calculate the used memory
		Runtime runtime = Runtime.getRuntime();
        log.info("memory from Runtime: total {} MB, max {} MB, free {} MB", runtime.totalMemory() / MB, runtime.maxMemory()/ MB, runtime.freeMemory()/ MB);
		
	}
}
