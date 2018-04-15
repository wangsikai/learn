package com.lanking.cloud.component.db.config;

import java.util.Properties;

public class HibernateConfigProperties {
	private String dialect;
	private String showSql;
	private String formatSql;
	private String hbm2ddlAuto;
	private String entitymanagerPackagesToScan;

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getShowSql() {
		return showSql;
	}

	public void setShowSql(String showSql) {
		this.showSql = showSql;
	}

	public String getFormatSql() {
		return formatSql;
	}

	public void setFormatSql(String formatSql) {
		this.formatSql = formatSql;
	}

	public String getHbm2ddlAuto() {
		return hbm2ddlAuto;
	}

	public void setHbm2ddlAuto(String hbm2ddlAuto) {
		this.hbm2ddlAuto = hbm2ddlAuto;
	}

	public String getEntitymanagerPackagesToScan() {
		return entitymanagerPackagesToScan;
	}

	public void setEntitymanagerPackagesToScan(String entitymanagerPackagesToScan) {
		this.entitymanagerPackagesToScan = entitymanagerPackagesToScan;
	}

	public Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", this.dialect);
		hibernateProperties.put("hibernate.show_sql", this.showSql);
		hibernateProperties.put("hibernate.format_sql", this.formatSql);
		hibernateProperties.put("hibernate.hbm2ddl.auto", this.hbm2ddlAuto);
		return hibernateProperties;
	}

}
