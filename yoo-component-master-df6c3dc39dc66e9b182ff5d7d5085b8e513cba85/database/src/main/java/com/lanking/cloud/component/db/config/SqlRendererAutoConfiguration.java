package com.lanking.cloud.component.db.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.component.db.sql.httl.HttlEngineFactoryBean;
import com.lanking.cloud.component.db.sql.httl.HttlEngineProperties;
import com.lanking.cloud.component.db.sql.httl.SqlRendererImpl;

import httl.Engine;

/**
 * SQL渲染相关Bean配置,与具体系统服务无关
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年7月27日
 */
@Configuration
@EnableConfigurationProperties(HttlEngineProperties.class)
public class SqlRendererAutoConfiguration {

	@Primary
	@Bean(initMethod = "mergeAndCheck")
	HttlEngineFactoryBean httlEngineFactoryBean(HttlEngineProperties properties) {
		HttlEngineFactoryBean httlEngineFactoryBean = new HttlEngineFactoryBean();
		httlEngineFactoryBean.setProperties(properties);
		return httlEngineFactoryBean;
	}

	@Primary
	@Bean
	public SqlRenderer sqlRenderer(Engine engine) throws Exception {
		SqlRendererImpl sqlRenderer = new SqlRendererImpl();
		sqlRenderer.setEngine(engine);
		return sqlRenderer;
	}
}
