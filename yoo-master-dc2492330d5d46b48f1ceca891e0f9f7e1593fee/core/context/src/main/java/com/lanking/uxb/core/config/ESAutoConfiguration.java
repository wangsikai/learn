package com.lanking.uxb.core.config;

import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lanking.cloud.component.searcher.config.ESTransportProperties;

@Configuration
public class ESAutoConfiguration {

	@Bean("transportClientProperties")
	@Qualifier("transportClientProperties")
	@ConfigurationProperties("yoo-cloud.es.transportClient")
	ESTransportProperties transportClientProperties() {
		return new ESTransportProperties();
	}

	@Bean(value = "transportClient", destroyMethod = "close")
	@Qualifier("transportClient")
	Client transportClient(@Qualifier("transportClientProperties") ESTransportProperties transportClientProperties)
			throws NumberFormatException, UnknownHostException {
		return transportClientProperties.initTransportClient();
	}

}
