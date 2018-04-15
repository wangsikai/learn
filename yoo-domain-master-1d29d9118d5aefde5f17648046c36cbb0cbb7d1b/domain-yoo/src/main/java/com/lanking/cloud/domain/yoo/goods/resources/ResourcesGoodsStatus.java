package com.lanking.cloud.domain.yoo.goods.resources;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 资源商品状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum ResourcesGoodsStatus implements Valueable {

	/**
	 * 草稿
	 */
	DRAFT(0),
	/**
	 * 取消发布(下架)
	 */
	UN_PUBLISH(1),
	/**
	 * 发布(上架)
	 */
	PUBLISH(2),
	/**
	 * 删除
	 */
	DELETE(3);

	private final int value;

	private ResourcesGoodsStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ResourcesGoodsStatus findByValue(int value) {
		switch (value) {
		case 0:
			return DRAFT;
		case 1:
			return UN_PUBLISH;
		case 2:
			return PUBLISH;
		case 3:
			return DELETE;
		default:
			return null;
		}
	}
}
