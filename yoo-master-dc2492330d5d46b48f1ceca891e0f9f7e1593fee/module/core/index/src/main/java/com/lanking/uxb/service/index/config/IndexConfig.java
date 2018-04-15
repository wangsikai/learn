package com.lanking.uxb.service.index.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lanking.uxb.service.index.api.impl.listener.MappingListenerProcessor;
import com.lanking.uxb.service.search.api.IndexConfigService;

@Configuration
public class IndexConfig {

	@Autowired
	private IndexConfigService indexConfigService;

	@Bean
	MappingListenerProcessor mappingListenerProcessor() {
		return new MappingListenerProcessor(indexConfigService);
	}
}
