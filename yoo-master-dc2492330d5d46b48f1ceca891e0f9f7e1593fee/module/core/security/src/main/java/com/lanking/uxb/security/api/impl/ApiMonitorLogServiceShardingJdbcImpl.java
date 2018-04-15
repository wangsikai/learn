package com.lanking.uxb.security.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.uxb.security.api.ApiMonitorLogService;

@Transactional(value = "shardingLogDataSourceTransactionManager", readOnly = true)
@Service
public class ApiMonitorLogServiceShardingJdbcImpl implements ApiMonitorLogService {

	@Autowired
	@Qualifier("shardingLogJdbcTemplate")
	private JdbcTemplate logJdbcTemplate;

	@Transactional("shardingLogDataSourceTransactionManager")
	@Override
	public void save(String api, int costTime, String ex, long userId, String params, String token, String hostName,
			Date createAt) {
		List<Object[]> batchArgs = Lists.newArrayList();
		Object[] batchArg = { SnowflakeUUID.next(), api, costTime, createAt, ex, params, userId, hostName, token };
		batchArgs.add(batchArg);
		logJdbcTemplate.update(getInsertSql(), batchArg);
	}

	@Transactional("shardingLogDataSourceTransactionManager")
	@Override
	public void save(List<String> apis, List<Integer> costTimes, List<String> exs, List<Long> userIds,
			List<String> params, List<String> tokens, List<String> hostNames, List<Date> createAts) {
		int size = apis.size();
		for (int i = 0; i < size; i++) {
			save(apis.get(i), costTimes.get(i), exs.get(i), userIds.get(i), params.get(i), tokens.get(i),
					hostNames.get(i), createAts.get(i));
		}
	}

	String getInsertSql() {
		final String insertSql = "INSERT INTO api_monitor_log (id,api,cost_time,create_at,ex,params,user_id,host_name,token)VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return insertSql;
	}
}
