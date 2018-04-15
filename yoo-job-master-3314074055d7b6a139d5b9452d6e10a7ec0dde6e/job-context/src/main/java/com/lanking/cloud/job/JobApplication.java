package com.lanking.cloud.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com" })
public class JobApplication {

	public static void main(final String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}
}
