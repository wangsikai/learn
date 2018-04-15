package com.lanking.cloud.domain.yoo.thirdparty;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 分享类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum ShareType implements Valueable {
	/**
	 * 内部分享
	 */
	INTERNAL(0),
	/**
	 * qq好友
	 */
	QQ_FRIEND(1),
	/**
	 * QQ空间
	 */
	QZONE(2),
	/**
	 * 微信好友
	 */
	WEIXIN_FRIEND(3),
	/**
	 * 微信朋友圈
	 */
	WEIXIN_FRIEND_CIRCLE(4);

	private int value;

	ShareType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static ShareType findByValue(int value) {
		switch (value) {
		case 0:
			return INTERNAL;
		case 1:
			return QQ_FRIEND;
		case 2:
			return QZONE;
		case 3:
			return WEIXIN_FRIEND;
		case 4:
			return WEIXIN_FRIEND_CIRCLE;
		default:
			return null;
		}
	}
}
