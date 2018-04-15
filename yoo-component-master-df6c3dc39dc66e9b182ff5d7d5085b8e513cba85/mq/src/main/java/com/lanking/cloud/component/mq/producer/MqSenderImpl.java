package com.lanking.cloud.component.mq.producer;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.SerializingConverter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lanking.cloud.component.mq.common.constants.MqInternalDelayConstants;
import com.lanking.cloud.component.mq.common.ex.MqException;
import com.lanking.cloud.sdk.util.Codecs;

public class MqSenderImpl implements MqSender {

	private Logger logger = LoggerFactory.getLogger(MqSenderImpl.class);

	private RabbitTemplate rabbitTemplate;
	private Executor taskExecutor;
	private static Converter<Object, byte[]> serializer = new SerializingConverter();

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void setTaskExecutor(Executor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void send(String exchange, MQ mq) throws MqException {
		send(exchange, "", mq);
	}

	@Override
	public void send(String exchange, String routingKey, MQ mq) throws MqException {
		send(exchange, routingKey, new MessageProperties(), mq);
	}

	@Override
	public void send(String exchange, String routingKey, long delay, MQ mq) throws MqException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("x-delay", delay);
		send(exchange, routingKey, messageProperties, mq);
	}

	@Override
	public void send(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException {
		props.setContentType(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT);
		if (mq.getDelay() > 0) {
			props.setHeader("x-delay", mq.getDelay());
			mq.setDelay(0);
			mq.setData(new DelayMQ(mq.getData(), exchange, routingKey));
			send(MqInternalDelayConstants.EX_INTERNAL_DELAY, MqInternalDelayConstants.RK_INTERNAL_DELAY, props, mq);
		} else {
			internalSend(exchange, routingKey, props, serializer.convert(mq.getData()));
		}
	}

	@Override
	public void sendText(String exchange, MQ mq) throws MqException {
		sendText(exchange, "", mq);
	}

	@Override
	public void sendText(String exchange, String routingKey, MQ mq) throws MqException {
		sendText(exchange, routingKey, new MessageProperties(), mq);

	}

	@Override
	public void sendText(String exchange, String routingKey, long delay, MQ mq) throws MqException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("x-delay", delay);
		sendText(exchange, routingKey, messageProperties, mq);

	}

	@Override
	public void sendText(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException {
		props.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		if (mq.getDelay() > 0) {
			props.setHeader("x-delay", mq.getDelay());
			mq.setDelay(0);
			mq.setData(new DelayMQ(mq.getData(), exchange, routingKey));
			sendText(MqInternalDelayConstants.EX_INTERNAL_DELAY, MqInternalDelayConstants.RK_INTERNAL_DELAY, props, mq);
		} else {
			internalSend(exchange, routingKey, props, Codecs.toBytes(mq.getData().toString()));
		}
	}

	@Override
	public void sendJSON(String exchange, MQ mq) throws MqException {
		sendJSON(exchange, "", mq);
	}

	@Override
	public void sendJSON(String exchange, String routingKey, MQ mq) throws MqException {
		sendJSON(exchange, routingKey, new MessageProperties(), mq);
	}

	@Override
	public void sendJSON(String exchange, String routingKey, long delay, MQ mq) throws MqException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("x-delay", delay);
		sendJSON(exchange, routingKey, messageProperties, mq);
	}

	@Override
	public void sendJSON(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException {
		props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		if (mq.getDelay() > 0) {
			props.setHeader("x-delay", mq.getDelay());
			mq.setDelay(0);
			mq.setData(new DelayMQ(mq.getData(), exchange, routingKey));
			sendJSON(MqInternalDelayConstants.EX_INTERNAL_DELAY, MqInternalDelayConstants.RK_INTERNAL_DELAY, props, mq);
		} else {
			internalSend(exchange, routingKey, props,
					JSON.toJSONBytes(mq.getData(), SerializerFeature.DisableCircularReferenceDetect));
		}
	}

	@Override
	public void asynSend(String exchange, MQ mq) throws MqException {
		asynSend(exchange, "", mq);
	}

	@Override
	public void asynSend(String exchange, String routingKey, MQ mq) throws MqException {
		asynSend(exchange, routingKey, new MessageProperties(), mq);
	}

	@Override
	public void asynSend(String exchange, String routingKey, long delay, MQ mq) throws MqException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("x-delay", delay);
		asynSend(exchange, routingKey, messageProperties, mq);
	}

	@Override
	public void asynSend(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException {
		props.setContentType(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT);
		if (mq.getDelay() > 0) {
			props.setHeader("x-delay", mq.getDelay());
			mq.setDelay(0);
			mq.setData(new DelayMQ(mq.getData(), exchange, routingKey));
			send(MqInternalDelayConstants.EX_INTERNAL_DELAY, MqInternalDelayConstants.RK_INTERNAL_DELAY, props, mq);
		} else {
			internalAsynSend(exchange, routingKey, props, serializer.convert(mq.getData()));
		}
	}

	@Override
	public void asynSendText(String exchange, MQ mq) throws MqException {
		asynSendText(exchange, "", mq);
	}

	@Override
	public void asynSendText(String exchange, String routingKey, MQ mq) throws MqException {
		asynSendText(exchange, routingKey, new MessageProperties(), mq);
	}

	@Override
	public void asynSendText(String exchange, String routingKey, long delay, MQ mq) throws MqException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("x-delay", delay);
		asynSendText(exchange, routingKey, messageProperties, mq);
	}

	@Override
	public void asynSendText(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException {
		props.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		if (mq.getDelay() > 0) {
			props.setHeader("x-delay", mq.getDelay());
			mq.setDelay(0);
			mq.setData(new DelayMQ(mq.getData(), exchange, routingKey));
			sendText(MqInternalDelayConstants.EX_INTERNAL_DELAY, MqInternalDelayConstants.RK_INTERNAL_DELAY, props, mq);
		} else {
			internalAsynSend(exchange, routingKey, props, Codecs.toBytes(mq.getData().toString()));
		}
	}

	@Override
	public void asynSendJSON(String exchange, MQ mq) throws MqException {
		asynSendJSON(exchange, "", mq);
	}

	@Override
	public void asynSendJSON(String exchange, String routingKey, MQ mq) throws MqException {
		asynSendJSON(exchange, routingKey, new MessageProperties(), mq);
	}

	@Override
	public void asynSendJSON(String exchange, String routingKey, long delay, MQ mq) throws MqException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("x-delay", delay);
		asynSendJSON(exchange, routingKey, messageProperties, mq);
	}

	@Override
	public void asynSendJSON(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException {
		props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		if (mq.getDelay() > 0) {
			props.setHeader("x-delay", mq.getDelay());
			mq.setDelay(0);
			mq.setData(new DelayMQ(mq.getData(), exchange, routingKey));
			sendJSON(MqInternalDelayConstants.EX_INTERNAL_DELAY, MqInternalDelayConstants.RK_INTERNAL_DELAY, props, mq);
		} else {
			internalAsynSend(exchange, routingKey, props,
					JSON.toJSONBytes(mq.getData(), SerializerFeature.DisableCircularReferenceDetect));
		}

	}

	private void internalSend(String exchange, String routingKey, MessageProperties props, byte[] bytes) {
		logger.info("exchange:[{}],routingKey:[{}]", exchange, routingKey);
		try {
			rabbitTemplate.send(exchange, routingKey, new Message(bytes, props));
		} catch (AmqpException e) {
			throw new MqException();
		}
	}

	private void internalAsynSend(final String exchange, final String routingKey, final MessageProperties props,
			final byte[] bytes) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				internalSend(exchange, routingKey, props, bytes);
			}
		});
	}

}
