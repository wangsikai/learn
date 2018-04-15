package com.lanking.uxb.core.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.component.db.support.jdbcTemplate.JdbcTemplate;

@Configuration
public class BiDataAutoConfiguration {
	@Bean("biDataSourceProperties")
	@Qualifier("biDataSourceProperties")
	@ConfigurationProperties("yoo-cloud.datasource.bi")
	DataSourceProperties biDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(value = "biDataSource", destroyMethod = "close")
	@Qualifier("biDataSource")
	@ConfigurationProperties("yoo-cloud.datasource.bi")
	DataSource biDataSource(@Qualifier("biDataSourceProperties") DataSourceProperties biDataSourceProperties) {
		return biDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean("biDataSourceTransactionManager")
	@Qualifier("biDataSourceTransactionManager")
	DataSourceTransactionManager biDataSourceTransactionManager(@Qualifier("biDataSource") DataSource biDataSource) {
		DataSourceTransactionManager biDataSourceTransactionManager = new DataSourceTransactionManager(biDataSource);
		return biDataSourceTransactionManager;
	}

	@Bean("biJdbcTemplate")
	@Qualifier("biJdbcTemplate")
	JdbcTemplate biJdbcTemplate(@Qualifier("biDataSource") DataSource biDataSource, SqlRenderer sqlRenderer) {
		return new JdbcTemplate(biDataSource, sqlRenderer);
	}
}
