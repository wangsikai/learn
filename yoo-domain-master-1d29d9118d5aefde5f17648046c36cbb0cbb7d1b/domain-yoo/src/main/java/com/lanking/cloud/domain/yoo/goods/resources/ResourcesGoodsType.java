package com.lanking.cloud.domain.yoo.goods.resources;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 资源商品类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum ResourcesGoodsType implements Valueable {
	/**
	 * 试卷
	 */
	EXAM_PAPER(0);

	private int value;

	ResourcesGoodsType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static ResourcesGoodsType findByValue(int value) {
		switch (value) {
		case 0:
			return EXAM_PAPER;
		default:
			return null;
		}
	}
}
