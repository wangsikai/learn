package com.lanking.cloud.springboot.listener;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import com.google.common.collect.Maps;

public abstract class AbstractConfigListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	public static Environment environment = null;
	public static final String COMMAND_LINE_ARGS = "commandLineArgs";
	public static final String CONFIG_SERVER = "CONFIG_SERVER";
	public static final String EXTERNAL_CONFIG_FILE = "EXTERNAL_CONFIG_FILE";

	public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

	public static Map<String, String> dynamicConfigs = Maps.newHashMap();
	public static PropertySource<?> commandLineArgsPropertySource = null;

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		ConfigurableEnvironment env = event.getEnvironment();
		if (commandLineArgsPropertySource == null) {
			commandLineArgsPropertySource = event.getEnvironment().getPropertySources().get(COMMAND_LINE_ARGS);
		}
		environment = env;
	}

	public static String getActiveProfile() {
		return environment.getProperty(SPRING_PROFILES_ACTIVE);
	}

	@SuppressWarnings("unchecked")
	Map<String, Object> property2Map(Properties props) {
		Map<String, Object> map = Maps.newHashMap();
		Enumeration<String> enu = (Enumeration<String>) props.propertyNames();
		while (enu.hasMoreElements()) {
			String key = enu.nextElement();
			map.put(key, props.get(key));
		}
		return map;
	}

	void reloadDynamicConfigs(Map<String, Object> configs) {
		synchronized (dynamicConfigs) {
			dynamicConfigs.clear();
			for (String key : configs.keySet()) {
				if (!commandLineArgsPropertySource.containsProperty(key)) {
					dynamicConfigs.put(key, configs.get(key).toString());
				}
			}
		}
	}
}
