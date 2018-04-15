package com.lanking.cloud.component.db.sharding.algorithm;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.lanking.cloud.ex.core.NotImplementedException;

public class SingleKeyByDateTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Date> {

	@Override
	public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Date> shardingValue) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(shardingValue.getValue());
		return new StringBuilder(shardingValue.getLogicTableName()).append("_").append(calendar.get(Calendar.YEAR))
				.append("_").append(calendar.get(Calendar.MONTH) + 1).append("_")
				.append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
	}

	@Override
	public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Date> shardingValue) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<String> doBetweenSharding(Collection<String> availableTargetNames,
			ShardingValue<Date> shardingValue) {
		throw new NotImplementedException();
	}
}
