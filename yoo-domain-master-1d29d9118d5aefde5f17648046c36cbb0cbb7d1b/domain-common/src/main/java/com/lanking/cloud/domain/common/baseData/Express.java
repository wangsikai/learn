package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Titleable;
import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 快递
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum Express implements Valueable, Titleable {
	/**
	 * 圆通
	 */
	YTO(0, "圆通"),
	/**
	 * 中通
	 */
	ZTO(1, "中通"),
	/**
	 * 顺丰
	 */
	SFN(2, "顺丰"),
	/**
	 * 韵达
	 */
	YDA(3, "韵达"),
	/**
	 * 申通
	 */
	STO(4, "申通"),
	/**
	 * 天天
	 */
	TT(5, "天天"),
	/**
	 * EMS
	 */
	EMS(6, "EMS"),
	/**
	 * 宅急送
	 */
	ZJS(7, "宅急送"),
	/**
	 * 百世快递
	 */
	BS(8, "百世快递");

	private int value;
	private String title;

	Express(int value, String title) {
		this.value = value;
		this.title = title;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getTitle() {
		return title;
	}

}
