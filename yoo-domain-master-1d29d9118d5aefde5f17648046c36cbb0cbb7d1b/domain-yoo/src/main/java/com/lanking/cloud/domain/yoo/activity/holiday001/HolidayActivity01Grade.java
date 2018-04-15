package com.lanking.cloud.domain.yoo.activity.holiday001;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 假期活动01-年级
 * 
 * <pre>
 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日)
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
public enum HolidayActivity01Grade implements Valueable {
	/**
	 * 初一
	 */
	PHASE_2_1(0, 2, "初一"),
	/**
	 * 初二
	 */
	PHASE_2_2(1, 2, "初二"),
	/**
	 * 初三
	 */
	PHASE_2_3(2, 2, "初三"),
	/**
	 * 高一
	 */
	PHASE_3_1(3, 3, "高一"),
	/**
	 * 高二
	 */
	PHASE_3_2(4, 3, "高二"),
	/**
	 * 高三
	 */
	PHASE_3_3(5, 3, "高三"),
	/**
	 * 初四
	 */
	PHASE_2_4(6, 2, "初四");
	

	private int value;
	private int phase;
	private String name;

	HolidayActivity01Grade(int value, int phase, String name) {
		this.value = value;
		this.phase = phase;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	public int getPhase() {
		return phase;
	}

	public String getName() {
		return name;
	}

}
