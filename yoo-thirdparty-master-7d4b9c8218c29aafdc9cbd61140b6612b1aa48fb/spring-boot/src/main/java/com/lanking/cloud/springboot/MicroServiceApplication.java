package com.lanking.cloud.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ResourceLoader;

import com.lanking.cloud.springboot.listener.ConfigServerListener;
import com.lanking.cloud.springboot.listener.ExternalConfigListener;

public final class MicroServiceApplication extends SpringApplication {

	public MicroServiceApplication(Object... sources) {
		super(sources);
		this.addListeners(new ExternalConfigListener());
		this.addListeners(new ConfigServerListener());
	}

	public MicroServiceApplication(ResourceLoader resourceLoader, Object... sources) {
		super(resourceLoader, sources);
		this.addListeners(new ExternalConfigListener());
		this.addListeners(new ConfigServerListener());
	}
}
