package com.lanking.uxb.service.thirdparty.weixin.client;

/**
 * 模板消息类型.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月4日
 */
public enum TemplateMessageType {

	/**
	 * 未知.
	 */
	UNKOWN(0),

	/**
	 * 布置作业消息（家长）.
	 */
	PUBLISH_HOMEWORK_TO_PARENT(1);

	private int value;

	TemplateMessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static TemplateMessageType findByValue(int value) {
		switch (value) {
		case 0:
			return UNKOWN;
		case 1:
			return PUBLISH_HOMEWORK_TO_PARENT;
		default:
			return UNKOWN;
		}
	}
}
