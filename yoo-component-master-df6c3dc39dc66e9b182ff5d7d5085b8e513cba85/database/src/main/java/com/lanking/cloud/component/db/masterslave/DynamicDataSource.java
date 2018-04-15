package com.lanking.cloud.component.db.masterslave;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return MasterSlaveContext.get();

	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
	}

}