package com.lanking.uxb.service.message.api.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.mq.common.constants.MqMessageRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.base.message.api.NoticePacket;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.message.api.MessageSender;

@Component
public class MessageSenderImpl implements MessageSender {

	private Logger logger = LoggerFactory.getLogger(MessageSenderImpl.class);

	@Autowired
	private MqSender mqSender;

	@Override
	public void send(MessagePacket packet) {
		boolean legal = true;
		// check message type
		String routingKey = getRoutingKey(packet.getMessageType());
		if (StringUtils.isBlank(routingKey)) {
			legal = false;
		}
		if (packet.getMessageType() == MessageType.PUSH) {
			PushPacket pushPacket = (PushPacket) packet;
			if (CollectionUtils.isEmpty(pushPacket.getTargets()) && StringUtils.isBlank(pushPacket.getTarget())) {
				legal = false;
			}
		} else if (packet.getMessageType() == MessageType.SMS) {
			SmsPacket smsPacket = (SmsPacket) packet;
			if (CollectionUtils.isEmpty(smsPacket.getTargets()) && StringUtils.isBlank(smsPacket.getTarget())) {
				legal = false;
			}
		} else if (packet.getMessageType() == MessageType.EMAIL) {
			EmailPacket emailPacket = (EmailPacket) packet;
			if (CollectionUtils.isEmpty(emailPacket.getTargets()) && StringUtils.isBlank(emailPacket.getTarget())) {
				legal = false;
			}
		} else if (packet.getMessageType() == MessageType.NOTICE) {
			NoticePacket noticePacket = (NoticePacket) packet;
			if (CollectionUtils.isEmpty(noticePacket.getTos()) && noticePacket.getTo() == 0) {
				legal = false;
			}
		}
		if (legal) {
			packet.setCreateAt(new Date());
			mqSender.send(MqMessageRegistryConstants.EX_MSG, routingKey, MQ.builder().data(packet).build());
		} else {
			logger.warn("not support message:{}", packet.toString());
		}
	}

	private String getRoutingKey(MessageType type) {
		if (MessageType.EMAIL == type) {
			return MqMessageRegistryConstants.RK_MSG_EMAIL;
		} else if (MessageType.SMS == type) {
			return MqMessageRegistryConstants.RK_MSG_SMS;
		} else if (MessageType.NOTICE == type) {
			return MqMessageRegistryConstants.RK_MSG_NOTICE;
		} else if (MessageType.PUSH == type) {
			return MqMessageRegistryConstants.RK_MSG_PUSH;
		}
		return null;
	}

}
