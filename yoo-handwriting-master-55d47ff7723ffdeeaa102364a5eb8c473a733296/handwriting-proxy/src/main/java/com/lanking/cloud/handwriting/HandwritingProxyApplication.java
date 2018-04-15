package com.lanking.cloud.handwriting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com" })
public class HandwritingProxyApplication {

	public static void main(final String[] args) {
		SpringApplication.run(HandwritingProxyApplication.class, args);
	}
}
