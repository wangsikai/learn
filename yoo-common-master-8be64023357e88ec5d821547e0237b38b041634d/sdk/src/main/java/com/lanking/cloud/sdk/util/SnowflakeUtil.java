package com.lanking.cloud.sdk.util;

import java.util.Date;

/**
 * 提供解析uuid的相关方法
 * 
 * @since 3.0.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年1月7日
 */
public final class SnowflakeUtil {

	public static long timestamp(long uuid) {
		try {
			String binary = Long.toBinaryString(uuid);
			return Long.parseLong(binary.substring(0, binary.length() - 23), 2) + Snowflake.EPOCH;
		} catch (Exception e) {
			return -1;
		}
	}

	public static Date date(long uuid) {
		try {
			return new Date(timestamp(uuid));
		} catch (Exception e) {
			return null;
		}
	}

	public static int node(long uuid) {
		try {
			String binary = Long.toBinaryString(uuid);
			int binaryLength = binary.length();
			return Integer.parseInt(binary.substring(binaryLength - 23, binaryLength - 13), 2);
		} catch (Exception e) {
			return -1;
		}
	}
}
