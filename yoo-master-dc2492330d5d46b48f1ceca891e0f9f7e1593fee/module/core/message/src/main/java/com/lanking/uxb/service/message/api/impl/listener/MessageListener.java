package com.lanking.uxb.service.message.api.impl.listener;

import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqMessageRegistryConstants;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.uxb.service.message.api.EmailService;
import com.lanking.uxb.service.message.api.PushService;
import com.lanking.uxb.service.message.api.SendProvider;
import com.lanking.uxb.service.message.api.SmsService;

@Component
@Exchange(name = MqMessageRegistryConstants.EX_MSG)
public class MessageListener implements ApplicationContextAware, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(MessageListener.class);

	private List<SendProvider> sendProviders = Lists.newArrayList();

	private ApplicationContext appContext;

	@Autowired
	private SmsService smsService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PushService pushService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@Listener(queue = MqMessageRegistryConstants.QUEUE_MSG_EMAIL, routingKey = MqMessageRegistryConstants.RK_MSG_EMAIL, series = false)
	public void email(MessagePacket packet) {
		internalSend(packet);
	}

	@Listener(queue = MqMessageRegistryConstants.QUEUE_MSG_SMS, routingKey = MqMessageRegistryConstants.RK_MSG_SMS, series = false)
	public void sms(MessagePacket packet) {
		internalSend(packet);
	}

	@Listener(queue = MqMessageRegistryConstants.QUEUE_MSG_NOTICE, routingKey = MqMessageRegistryConstants.RK_MSG_NOTICE, series = false)
	public void notice(MessagePacket packet) {
		internalSend(packet);
	}

	@Listener(queue = MqMessageRegistryConstants.QUEUE_MSG_PUSH, routingKey = MqMessageRegistryConstants.RK_MSG_PUSH, series = false)
	public void push(MessagePacket packet) {
		internalSend(packet);
	}

	@Listener(queue = MqMessageRegistryConstants.QUEUE_MSG_SAVE, routingKey = MqMessageRegistryConstants.RK_MSG_SAVE, series = false)
	public void save(MessagePacket packet) {
		logger.info("save message:{}", packet.toString());
		try {
			if (packet.getMessageType() == MessageType.SMS) {
				smsService.save((SmsPacket) packet);
			} else if (packet.getMessageType() == MessageType.EMAIL) {
				emailService.save((EmailPacket) packet);
			} else if (packet.getMessageType() == MessageType.PUSH) {
				pushService.save((PushPacket) packet);
			}
		} catch (Exception e) {
			logger.error("save message error:", e);
		}
	}

	@Listener(queue = MqMessageRegistryConstants.QUEUE_MSG_TEMPLATE, routingKey = MqMessageRegistryConstants.RK_MSG_TEMPLATE, series = false)
	public void processTemplate(JSONObject jsonObject) {
		logger.info("process template:{}", jsonObject.toString());
		// TODO 目前只有单个node接受message mq,如果改成无中心node的方式后,发送刷新消息模板的地方需要修改
		try {
			String action = jsonObject.getString("action");
			if ("clearCache".equals(action)) {
				MessageType messageType = jsonObject.getObject("messageType", MessageType.class);
				for (SendProvider sendProvider : sendProviders) {
					try {
						if (sendProvider.accept(messageType)) {
							sendProvider.refreshTemplate();
						}
					} catch (Exception e) {
						logger.error("refresh template error:", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("process template error:", e);
		}
	}

	private void internalSend(final MessagePacket packet) {
		logger.info("receive mq packet:{}", packet.getMessageType());
		executor.execute(new Runnable() {
			@Override
			public void run() {
				for (SendProvider sendProvider : sendProviders) {
					try {
						if (sendProvider.accept(packet.getMessageType())) {
							sendProvider.send(packet);
						}
					} catch (Exception e) {
						logger.error("send msg fail:", e);
					}
				}

			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (SendProvider sendProvider : appContext.getBeansOfType(SendProvider.class).values()) {
			sendProviders.add(sendProvider);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
}
