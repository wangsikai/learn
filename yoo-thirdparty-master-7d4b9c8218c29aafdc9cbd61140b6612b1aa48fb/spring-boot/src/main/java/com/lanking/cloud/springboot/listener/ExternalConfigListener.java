package com.lanking.cloud.springboot.listener;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月8日
 */
public final class ExternalConfigListener extends AbstractConfigListener {

	private Logger logger = LoggerFactory.getLogger(ExternalConfigListener.class);

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		super.onApplicationEvent(event);
		ConfigurableEnvironment env = event.getEnvironment();

		// 当前服务的外部配置文件
		String serviceExternalConfigFile = env.getProperty("yoo-cloud.service.externalConfigFile");
		if (new File(serviceExternalConfigFile).exists()) {
			Map<String, Object> serviceExternalConfig = null;
			try {
				serviceExternalConfig = property2Map(
						PropertiesLoaderUtils.loadProperties(new FileSystemResource(serviceExternalConfigFile)));
			} catch (IOException e) {
				logger.error("load external config file error:", e);
			}
			if (serviceExternalConfig != null) {
				event.getEnvironment().getPropertySources().addAfter(COMMAND_LINE_ARGS,
						new MapPropertySource(EXTERNAL_CONFIG_FILE, serviceExternalConfig));
			}
		} else {
			logger.info("service externalConfigFile[{}] not exists...", serviceExternalConfigFile);
		}

		environment = env;
	}
}
