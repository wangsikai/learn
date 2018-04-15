package com.lanking.cloud.domain.base.counter.api;


/**
 * 对应counter、counter_detail表里面的字段名称
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public enum Count {
	/**
	 * count_1
	 */
	COUNTER_1("count_1"),
	/**
	 * count_2
	 */
	COUNTER_2("count_2"),
	/**
	 * count_3
	 */
	COUNTER_3("count_3"),
	/**
	 * count_4
	 */
	COUNTER_4("count_4"),
	/**
	 * count_5
	 */
	COUNTER_5("count_5"),
	/**
	 * count_6
	 */
	COUNTER_6("count_6"),
	/**
	 * count_7
	 */
	COUNTER_7("count_7"),
	/**
	 * count_8
	 */
	COUNTER_8("count_8"),
	/**
	 * count_9
	 */
	COUNTER_9("count_9"),
	/**
	 * count_10
	 */
	COUNTER_10("count_10"),
	/**
	 * count_11
	 */
	COUNTER_11("count_11"),
	/**
	 * count_12
	 */
	COUNTER_12("count_12"),

	/**
	 * count_13
	 */
	COUNTER_13("count_13"),
	/**
	 * count_14
	 */
	COUNTER_14("count_14"),
	/**
	 * count_15
	 */
	COUNTER_15("count_15"),
	/**
	 * count_16
	 */
	COUNTER_16("count_16"),
	/**
	 * count_17
	 */
	COUNTER_17("count_17"),
	/**
	 * count_18
	 */
	COUNTER_18("count_18"),
	/**
	 * count_19
	 */
	COUNTER_19("count_19"),
	/**
	 * count_20
	 */
	COUNTER_20("count_20"),
	/**
	 * count_21
	 */
	COUNTER_21("count_21"),
	/**
	 * count_22
	 */
	COUNTER_22("count_22"),
	/**
	 * count_23
	 */
	COUNTER_23("count_23"),
	/**
	 * count_24
	 */
	COUNTER_24("count_24");

	private String value;

	Count(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	/**
	 * 获取对应的计数字段更新时间字段名称
	 * 
	 * @param count
	 *            {@link Count}
	 * @return 更新时间字段名称
	 */
	public static String getUpdateAt(Count count) {
		return "update_at_" + count.getValue().split("_")[1];
	}

}
