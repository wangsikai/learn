package com.lanking.cloud.domain.yoo.goods.coins;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 金币商品商品类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CoinsGoodsType implements Valueable {
	/**
	 * 手机话费,同TELEPHONE_FLOW通用
	 */
	TELEPHONE_CHARGE(0),
	/**
	 * qq会员
	 */
	QQ_VIP(1),

	/**
	 * 金币
	 */
	COINS(2),

	/**
	 * 鸡汤鼓励,谢谢参与等.
	 */
	NOTHING(3),

	/**
	 * 现金.
	 */
	CASH(4),

	/**
	 * 手机流量,同TELEPHONE_CHARGE通用（目前只在活动中出现过）禁用
	 */
	@Deprecated
	TELEPHONE_FLOW(5),

	/**
	 * 线上卡券
	 */
	COUPONS(6),

	/**
	 * 实物商品
	 */
	PHYSICAL_COMMODITY(7),

	/**
	 * 其他, 数据库目前直接存null没有类型，这个枚举只用于VO中
	 */
	NULL(100);

	private int value;

	CoinsGoodsType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static CoinsGoodsType findByValue(int value) {
		switch (value) {
		case 0:
			return TELEPHONE_CHARGE;
		case 1:
			return QQ_VIP;
		case 2:
			return COINS;
		case 3:
			return NOTHING;
		case 4:
			return CASH;
		case 5:
			return TELEPHONE_FLOW;
		case 6:
			return COUPONS;
		case 7:
			return PHYSICAL_COMMODITY;
		case 100:
			return NULL;
		default:
			return null;
		}
	}
}
