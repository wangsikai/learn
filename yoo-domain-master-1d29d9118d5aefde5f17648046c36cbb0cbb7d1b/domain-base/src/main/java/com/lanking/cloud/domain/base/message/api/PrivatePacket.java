package com.lanking.cloud.domain.base.message.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lanking.cloud.domain.base.message.api.MessageType;

/**
 * 私信消息包
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class PrivatePacket extends MessagePacket {

	private static final long serialVersionUID = 233651675068280802L;

	public PrivatePacket() {
		super();
		setMessageType(MessageType.PRIVATE);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
