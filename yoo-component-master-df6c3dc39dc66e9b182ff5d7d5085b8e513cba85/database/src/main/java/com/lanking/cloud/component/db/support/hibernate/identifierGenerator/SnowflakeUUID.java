package com.lanking.cloud.component.db.support.hibernate.identifierGenerator;

import com.lanking.cloud.sdk.util.Snowflake;

public final class SnowflakeUUID {
	private static Snowflake idWorker;

	public static long next() {
		return idWorker.nextId();
	}

	private SnowflakeUUID() {
	}

	public static void init(int workID) {
		idWorker = Snowflake.getInstance(workID);
	}
}
