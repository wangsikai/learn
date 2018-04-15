package com.lanking.cloud.domain.yoo.goods.resources;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 资源商品分类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum ResourcesGoodsCategory implements Valueable {
	/**
	 * 普通
	 */
	COMMON(0),

	/**
	 * 推荐
	 */
	RECOMMEND(1);

	private int value;

	ResourcesGoodsCategory(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static ResourcesGoodsCategory findByValue(int value) {
		switch (value) {
		case 0:
			return COMMON;
		case 1:
			return RECOMMEND;
		default:
			return COMMON;
		}
	}
}
