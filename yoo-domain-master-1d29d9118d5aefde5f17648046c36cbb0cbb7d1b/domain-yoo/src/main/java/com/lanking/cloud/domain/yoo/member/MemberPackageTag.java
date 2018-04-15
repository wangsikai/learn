package com.lanking.cloud.domain.yoo.member;

import com.lanking.cloud.sdk.bean.Titleable;
import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员套餐标签
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberPackageTag implements Valueable, Titleable {
	/**
	 * 推荐
	 */
	RECOMMEND(0, "推荐"),
	/**
	 * 超值
	 */
	BARGAIN(1, "超值"),
	/**
	 * 活动
	 */
	ACTIVITY(2, "活动");

	private int value;
	private String title;

	MemberPackageTag(int value, String title) {
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
