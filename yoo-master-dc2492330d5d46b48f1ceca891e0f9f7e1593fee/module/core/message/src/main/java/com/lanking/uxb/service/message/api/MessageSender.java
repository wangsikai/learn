package com.lanking.uxb.service.message.api;

import com.lanking.cloud.domain.base.message.api.MessagePacket;

/**
 * 消息发送api
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月7日
 */
public interface MessageSender {

	void send(MessagePacket messagePacket);

}