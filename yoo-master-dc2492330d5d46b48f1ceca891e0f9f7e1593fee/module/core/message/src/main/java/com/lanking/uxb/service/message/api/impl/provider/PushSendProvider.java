package com.lanking.uxb.service.message.api.impl.provider;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.message.PushTemplate;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.message.api.impl.push.PushSender;
import com.lanking.uxb.service.message.ex.MessageException;

/**
 * 推送provider
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月23日
 */
@Component
public class PushSendProvider extends AbstractSendProvider {

	private Logger logger = LoggerFactory.getLogger(PushSendProvider.class);

	@Autowired
	private PushSender pushSender;

	@Override
	public boolean accept(MessageType messageType) {
		return messageType == getType();
	}

	@Override
	public MessageType getType() {
		return MessageType.PUSH;
	}

	@Override
	String getTitleTemplate(int code) throws Exception {
		PushTemplate pushTemplate = messageTemplateCacheService.getPushTemplate(code);
		if (pushTemplate == null) {
			pushTemplate = messageTemplateService.getPushTemplate(code);
			if (pushTemplate == null) {
				throw new Exception();
			}
			messageTemplateCacheService.putPushTemplate(pushTemplate);
		}
		return pushTemplate.getTitle();
	}

	@Override
	String getBodyTemplate(int code) throws Exception {
		PushTemplate pushTemplate = messageTemplateCacheService.getPushTemplate(code);
		if (pushTemplate == null) {
			pushTemplate = messageTemplateService.getPushTemplate(code);
			if (pushTemplate == null) {
				throw new Exception();
			}
			messageTemplateCacheService.putPushTemplate(pushTemplate);
		}
		return pushTemplate.getBody();
	}

	@Override
	boolean isMock(int code) throws Exception {
		throw new Exception();
	}

	@Override
	public void send(MessagePacket packet) throws MessageException {
		try {
			PushPacket pushPacket = (PushPacket) packet;
			pushPacket.setRetMsgs(new HashMap<String, String>());
			logger.info("receive mq packet:{}", pushPacket.toString());
			if (pushPacket.getMessageTemplateCode() != null && pushPacket.getMessageTemplateBodyParams() != null) {
				pushPacket.setTitle(getTitle(pushPacket.getMessageTemplateCode(),
						pushPacket.getMessageTemplateTitleParams()));
				pushPacket.setBody(getBody(pushPacket.getMessageTemplateCode(),
						pushPacket.getMessageTemplateBodyParams()));
			}
			if (CollectionUtils.isNotEmpty(pushPacket.getTargets())) {
				for (String target : pushPacket.getTargets()) {
					if (target.length() == 64) {
						pushPacket.getRetMsgs().put(
								target,
								pushSender.push2IOS(pushPacket.getProduct(), pushPacket.getApp(), target,
										pushPacket.getBody(), pushPacket.getParams()));
					} else {
						pushPacket.getRetMsgs().put(
								target,
								pushSender.push2Android(pushPacket.getProduct(), pushPacket.getApp(), target,
										pushPacket.getTitle(), pushPacket.getBody(), pushPacket.getParams()));
					}
				}
			} else {
				if (pushPacket.getTarget().length() == 64) {
					pushPacket.setRetMsg(pushSender.push2IOS(pushPacket.getProduct(), pushPacket.getApp(),
							pushPacket.getTarget(), pushPacket.getBody(), pushPacket.getParams()));
				} else {
					pushPacket
							.setRetMsg(pushSender.push2Android(pushPacket.getProduct(), pushPacket.getApp(),
									pushPacket.getTarget(), pushPacket.getTitle(), pushPacket.getBody(),
									pushPacket.getParams()));
				}
			}
			afterSend(pushPacket);
		} catch (Exception e) {
			logger.error("send sms fail:", e);
		}
	}
}
