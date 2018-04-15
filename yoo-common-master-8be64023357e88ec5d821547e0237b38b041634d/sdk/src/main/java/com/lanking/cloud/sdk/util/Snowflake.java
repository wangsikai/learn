package com.lanking.cloud.sdk.util;

/**
 * 支持排序的高可靠64位主键生成器 类似twitter snowflake主键算法的变体 需要通过配置workerId达到不同机器生成不同id序列
 * 毫秒级时间(40位)+机器id(10位)+毫秒内序列(13位) 只能支持35年的不重复序列,但是这个是问题吗? 参考:
 * https://github.com/twitter/snowflake
 * <p/>
 *
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月17日
 */
public final class Snowflake {
	public final static long EPOCH = 1408800000000l;
	private final static int WORKER_ID_BITS = 10;
	private final static int COUNTER_BITS = 13;
	private final static long TIMESTAMP_SHIFT = WORKER_ID_BITS + COUNTER_BITS;
	public final static long COUNTER_MASK = (1 << COUNTER_BITS) - 1;

	private final int workerId;
	private int counter = 0;
	private long lastTimestamp = -1l;

	private Snowflake(int workerId) {
		this.workerId = workerId;
		if (workerId < 0 && workerId > 1023) {
			throw new IllegalArgumentException("worker Id can't be greater than 1023 or less than 0");
		}
	}

	public synchronized long nextId() {
		long timestamp = System.currentTimeMillis();
		if (timestamp > lastTimestamp) {
			counter = 0;
			lastTimestamp = timestamp;
		} else if (lastTimestamp > timestamp) {
			throw new IllegalStateException("Clock moved backwards, Refusing to generate id for "
					+ (lastTimestamp - timestamp) + " milliseconds");
		}
		return timestamp - EPOCH << TIMESTAMP_SHIFT | workerId << COUNTER_BITS | counter++ & COUNTER_MASK;
	}

	public static Snowflake getInstance(int workerId) {
		return new Snowflake(workerId);
	}
}
