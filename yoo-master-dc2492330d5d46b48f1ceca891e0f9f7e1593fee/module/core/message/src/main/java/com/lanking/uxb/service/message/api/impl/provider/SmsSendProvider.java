package com.lanking.uxb.service.message.api.impl.provider;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.message.SmsTemplate;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.message.api.impl.sms.SmsSender;
import com.lanking.uxb.service.message.ex.MessageException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
@Component
public class SmsSendProvider extends AbstractSendProvider {
	private Logger logger = LoggerFactory.getLogger(SmsSendProvider.class);

	@Autowired
	private SmsSender smsSender;

	@Override
	public boolean accept(MessageType messageType) {
		return messageType == getType();
	}

	@Override
	public MessageType getType() {
		return MessageType.SMS;
	}

	@Override
	String getTitleTemplate(int code) throws Exception {
		throw new Exception();
	}

	@Override
	String getBodyTemplate(int code) throws Exception {
		SmsTemplate smsTemplate = messageTemplateCacheService.getSmsTemplate(code);
		if (smsTemplate == null) {
			smsTemplate = messageTemplateService.getSmsTemplate(code);
			if (smsTemplate == null) {
				throw new Exception();
			}
			messageTemplateCacheService.putSmsTemplate(smsTemplate);
		}
		return smsTemplate.getBody();
	}

	@Override
	boolean isMock(int code) throws Exception {
		SmsTemplate smsTemplate = messageTemplateCacheService.getSmsTemplate(code);
		if (smsTemplate == null) {
			smsTemplate = messageTemplateService.getSmsTemplate(code);
			if (smsTemplate == null) {
				throw new Exception();
			}
			messageTemplateCacheService.putSmsTemplate(smsTemplate);
		}
		return smsTemplate.isMock();
	}

	@Override
	public void send(MessagePacket packet) throws MessageException {
		try {
			SmsPacket smsPacket = (SmsPacket) packet;
			logger.info("receive mq packet:{}", smsPacket.toString());
			if (smsPacket.getMessageTemplateCode() != null && smsPacket.getMessageTemplateBodyParams() != null) {
				smsPacket
						.setBody(getBody(smsPacket.getMessageTemplateCode(), smsPacket.getMessageTemplateBodyParams()));
			}
			if (StringUtils.isNotBlank(smsPacket.getBody())) {
				if (isMock(smsPacket.getMessageTemplateCode())) {
					smsPacket.setRet(-2);
				} else {
					Collection<String> targets = null;
					if (CollectionUtils.isNotEmpty(smsPacket.getTargets())) {
						targets = smsPacket.getTargets();
					} else {
						targets = Lists.newArrayList(smsPacket.getTarget());
					}
					smsPacket.setCallRet(smsSender.send(targets, smsPacket.getBody().split("】")[0].replaceAll("【", ""),
							smsPacket.getBody().split("#alidayu#")[1], smsPacket.getMessageTemplateBodyParams()));
				}
			} else {
				smsPacket.setRet(-1);
			}
			afterSend(smsPacket);
		} catch (Exception e) {
			logger.error("send sms fail:", e);
		}
	}

}
