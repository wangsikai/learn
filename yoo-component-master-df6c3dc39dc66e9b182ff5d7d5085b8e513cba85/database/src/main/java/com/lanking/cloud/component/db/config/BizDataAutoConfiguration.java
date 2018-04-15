package com.lanking.cloud.component.db.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.component.db.support.hibernate.HibernateRepoFactory;
import com.lanking.cloud.component.db.support.hibernate.RepoFactory;
import com.lanking.cloud.component.db.support.jdbcTemplate.JdbcTemplate;

/**
 * config example
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年7月27日
 */
@Deprecated
public class BizDataAutoConfiguration {

	@Primary
	@Bean("bizDataSourceProperties")
	@Qualifier("bizDataSourceProperties")
	@ConfigurationProperties("yoo-cloud.datasource.biz")
	DataSourceProperties bizDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(value = "bizDatasource", destroyMethod = "close")
	@Qualifier("bizDataSource")
	@ConfigurationProperties("yoo-cloud.datasource.biz")
	DataSource bizDataSource(@Qualifier("bizDataSourceProperties") DataSourceProperties bizDataSourceProperties) {
		DataSource dataSource = bizDataSourceProperties.initializeDataSourceBuilder().build();
		return dataSource;
	}

	@Bean("bizDataSourceTransactionManager")
	@Qualifier("bizDataSourceTransactionManager")
	DataSourceTransactionManager bizDataSourceTransactionManager(@Qualifier("bizDataSource") DataSource bizDataSource) {
		DataSourceTransactionManager bizDataSourceTransactionManager = new DataSourceTransactionManager(bizDataSource);
		return bizDataSourceTransactionManager;
	}

	@Primary
	@Bean("hibernateConfigProperties")
	@Qualifier("hibernateConfigProperties")
	@ConfigurationProperties("yoo-cloud.hibernate.biz")
	HibernateConfigProperties hibernateConfigProperties() {
		return new HibernateConfigProperties();
	}

	@Primary
	@Bean("sessionFactory")
	@Qualifier("sessionFactory")
	@ConfigurationProperties("yoo-cloud.hibernate.biz")
	LocalSessionFactoryBean sessionFactory(@Qualifier("bizDataSource") DataSource bizDataSource,
			@Qualifier("hibernateConfigProperties") HibernateConfigProperties hibernateConfigProperties) {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(bizDataSource);
		sessionFactoryBean.setPackagesToScan(hibernateConfigProperties.getEntitymanagerPackagesToScan());
		sessionFactoryBean.setHibernateProperties(hibernateConfigProperties.hibernateProperties());
		return sessionFactoryBean;
	}

	@Primary
	@Bean("transactionManager")
	@Qualifier("transactionManager")
	HibernateTransactionManager transactionManager(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}

	@Primary
	@Bean("repoFactory")
	@Qualifier("repoFactory")
	public RepoFactory repoFactory(SqlRenderer sqlRenderer, @Qualifier("sessionFactory") SessionFactory sessionFactory)
			throws Exception {
		HibernateRepoFactory hibernateRepoFactory = new HibernateRepoFactory();
		hibernateRepoFactory.setSqlRenderer(sqlRenderer);
		hibernateRepoFactory.setSessionFactory(sessionFactory);
		return hibernateRepoFactory;
	}

	@Primary
	@Bean("bizJdbcTemplate")
	@Qualifier("bizJdbcTemplate")
	JdbcTemplate jdbcTemplate(@Qualifier("bizDataSource") DataSource bizDataSource, SqlRenderer sqlRenderer) {
		return new JdbcTemplate(bizDataSource, sqlRenderer);
	}
}
