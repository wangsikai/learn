package com.lanking.cloud.domain.yoo.common;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * banner 位置定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum BannerLocation implements Valueable {
	/**
	 * 应用首页
	 */
	HOME(0),
	/**
	 * 金币商城首页
	 */
	COINS_MALL(1),
	/**
	 * 金币商城首页抽奖图片
	 */
	COINS_MALL_LUCKDRAW(2),
	/**
	 * 启动画面
	 */
	SPLASH_SCREEN(3),
	/**
	 * 资源
	 */
	RESOURCES(4);

	private int value;

	BannerLocation(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
