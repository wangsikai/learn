package com.lanking.uxb.service.code.api.impl.listener;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.lanking.uxb.service.code.api.BaseDataService;
import com.lanking.uxb.service.code.api.BaseDataType;

@Component
public class InitDataListener implements ApplicationListener<ApplicationReadyEvent> {

	private Logger logger = LoggerFactory.getLogger(InitDataListener.class);

	@Autowired
	private BaseDataService baseDataService;
	
	private Integer refreshCnt = 0;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		refreshCnt++;
		logger.info("ApplicationReadyEvent cnt {}", refreshCnt);
		logger.info("ApplicationReadyEvent: id/{}, appName/{}", event.getApplicationContext().getId(), event.getApplicationContext().getApplicationName());
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
		logger.info("before init memory object,heap memory usage:{} MB", memoryUsage.getUsed() / 1024 / 1024);
		for (BaseDataType baseDataType : BaseDataType.values()) {
			baseDataService.init(baseDataType);
		}
		logger.info("after init memory object,heap memory usage:{} MB", ManagementFactory.getMemoryMXBean()
				.getHeapMemoryUsage().getUsed() / 1024 / 1024);

	}
}
