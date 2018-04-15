package com.lanking.cloud.domain.yoo.honor.point;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 积分规则
 * 
 * <pre>
 * 目前悠数学中没有积分的业务，原来悠学中遗留下来的
 * http://dev.elanking.com:8088/wiki/pages/viewpage.action?pageId=10158257
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum PointAction implements Valueable {
	NULL(0);

	private final int value;

	private PointAction(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
