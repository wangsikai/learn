package com.lanking.cloud.component.db.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = { "${yoo-cloud.datasource.config.locations}", "${yoo-cloud.hibernate.config.locations}",
		"${yoo-cloud.jdbcTemplate.config.locations}", "${yoo-cloud.hibernate.repo.config.locations}" })
public class DataAutoConfiguration {

}
