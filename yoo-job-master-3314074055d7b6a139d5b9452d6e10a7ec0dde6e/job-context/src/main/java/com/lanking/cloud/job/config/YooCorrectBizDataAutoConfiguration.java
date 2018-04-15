package com.lanking.cloud.job.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.lanking.cloud.component.db.config.HibernateConfigProperties;
import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.component.db.support.hibernate.HibernateRepoFactory;
import com.lanking.cloud.component.db.support.hibernate.RepoFactory;
import com.lanking.cloud.domain.yoomath.homework.CorrectQuestionSource;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:小优快批数据源相关配置
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月7日
 * @since 小优快批
 */
@Configuration
public class YooCorrectBizDataAutoConfiguration {
	@Primary
	@Bean("yooCorrectDataSourceProperties")
	@Qualifier("yooCorrectDataSourceProperties")
	@ConfigurationProperties("yoo-cloud.job.datasource.yoo-correct")
	DataSourceProperties yooCorrectDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(value = "yooCorrectBizDataSource", destroyMethod = "close")
	@Qualifier("yooCorrectBizDataSource")
	DataSource yooCorrectBizDataSource(
			@Qualifier("yooCorrectDataSourceProperties") DataSourceProperties yooCorrectDataSourceProperties) {
		DataSource dataSource = yooCorrectDataSourceProperties.initializeDataSourceBuilder().build();
		return dataSource;
	}

	@Primary
	@Bean("yooCorrectSessionFactory")
	@Qualifier("yooCorrectSessionFactory")
	LocalSessionFactoryBean yooCorrectSessionFactory(
			@Qualifier("yooCorrectBizDataSource") DataSource yooCorrectBizDataSource,
			@Qualifier("hibernateConfigProperties") HibernateConfigProperties hibernateConfigProperties) {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(yooCorrectBizDataSource);
		sessionFactoryBean.setPackagesToScan(hibernateConfigProperties.getEntitymanagerPackagesToScan());
		sessionFactoryBean.setHibernateProperties(hibernateConfigProperties.hibernateProperties());
		return sessionFactoryBean;
	}

	@Primary
	@Bean("yooCorrectRepoFactory")
	@Qualifier("yooCorrectRepoFactory")
	public RepoFactory yooCorrectRepoFactory(SqlRenderer sqlRenderer,
			@Qualifier("yooCorrectSessionFactory") SessionFactory yooCorrectSessionFactory) throws Exception {
		HibernateRepoFactory hibernateRepoFactory = new HibernateRepoFactory();
		hibernateRepoFactory.setSqlRenderer(sqlRenderer);
		hibernateRepoFactory.setSessionFactory(yooCorrectSessionFactory);
		return hibernateRepoFactory;
	}

	@Bean("yooCorrectTransactionManager")
	@Qualifier("yooCorrectTransactionManager")
	HibernateTransactionManager yooCorrectTransactionManager(
			@Qualifier("yooCorrectSessionFactory") SessionFactory yooCorrectSessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(yooCorrectSessionFactory);
		return transactionManager;
	}
}
