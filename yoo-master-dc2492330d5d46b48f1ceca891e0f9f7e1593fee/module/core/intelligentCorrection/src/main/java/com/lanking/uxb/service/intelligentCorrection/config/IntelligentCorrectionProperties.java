package com.lanking.uxb.service.intelligentCorrection.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "intelligentCorrection")
public class IntelligentCorrectionProperties {

	private String restAPIUSA;

	public String getRestAPIUSA() {
		return restAPIUSA;
	}

	public void setRestAPIUSA(String restAPIUSA) {
		this.restAPIUSA = restAPIUSA;
	}

}
