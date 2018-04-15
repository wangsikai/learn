package com.lanking.uxb.zycon.wx.value;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 按钮关联内容类型.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public enum RelationType implements Valueable {
	DEFUALT(0),
	/**
	 * 客服消息.
	 */
	CUSTOM_MESSAGE(1),
	/**
	 * 跳转外链.
	 */
	VIEW(2),

	/**
	 * 图文素材.
	 */
	NEWS(3);

	private int value;

	RelationType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
