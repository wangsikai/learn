package com.lanking.uxb.service.wx.resource;

/**
 * 微信消息响应类型.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月5日
 */
public enum MessageEventType {
	UNKOWN(""), REPORT_CODE("我要兑换码");

	private String value;

	MessageEventType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
