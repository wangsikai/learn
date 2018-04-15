/**
 * 
 */
package com.lanking.uxb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.spring.web.plugins.Docket;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * swagger config for rest api documentation
 * @author michael
 * @see http://localhost:8080/swagger-ui.html
 */

@Configuration
@EnableSwagger2
@Profile("swagger")
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors
						.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("Spring Boot REST API", "Spring Boot REST API for Yoomath UXB Service", "1.0",
				"Terms of service", new Contact("UXB", "http://www.elanking.com", "hr@elanking.com"),
				"Copyright © 2018 Lanking Tech Co.Ltd. 蓝舰信息科技南京有限公司版权所有", "https://www.elanking.com");
		return apiInfo;
	}

}
