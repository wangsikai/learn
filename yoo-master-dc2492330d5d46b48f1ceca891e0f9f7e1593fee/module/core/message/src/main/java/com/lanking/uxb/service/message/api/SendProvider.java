package com.lanking.uxb.service.message.api;

import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.uxb.service.message.ex.MessageException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
public interface SendProvider {

	boolean accept(MessageType messageType);

	MessageType getType();

	void send(MessagePacket packet) throws MessageException;

	void refreshTemplate();
}