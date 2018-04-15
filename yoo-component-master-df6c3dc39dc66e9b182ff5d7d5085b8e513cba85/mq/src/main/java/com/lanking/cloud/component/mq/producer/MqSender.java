package com.lanking.cloud.component.mq.producer;

import org.springframework.amqp.core.MessageProperties;

import com.lanking.cloud.component.mq.common.ex.MqException;

public interface MqSender {
	// 同步
	void send(String exchange, MQ mq) throws MqException;

	void send(String exchange, String routingKey, MQ mq) throws MqException;

	void send(String exchange, String routingKey, long delay, MQ mq) throws MqException;

	void send(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException;

	void sendText(String exchange, MQ mq) throws MqException;

	void sendText(String exchange, String routingKey, MQ mq) throws MqException;

	void sendText(String exchange, String routingKey, long delay, MQ mq) throws MqException;

	void sendText(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException;

	void sendJSON(String exchange, MQ mq) throws MqException;

	void sendJSON(String exchange, String routingKey, MQ mq) throws MqException;

	void sendJSON(String exchange, String routingKey, long delay, MQ mq) throws MqException;

	void sendJSON(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException;

	// 异步
	void asynSend(String exchange, MQ mq) throws MqException;

	void asynSend(String exchange, String routingKey, MQ mq) throws MqException;

	void asynSend(String exchange, String routingKey, long delay, MQ mq) throws MqException;

	void asynSend(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException;

	void asynSendText(String exchange, MQ mq) throws MqException;

	void asynSendText(String exchange, String routingKey, MQ mq) throws MqException;

	void asynSendText(String exchange, String routingKey, long delay, MQ mq) throws MqException;

	void asynSendText(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException;

	void asynSendJSON(String exchange, MQ mq) throws MqException;

	void asynSendJSON(String exchange, String routingKey, MQ mq) throws MqException;

	void asynSendJSON(String exchange, String routingKey, long delay, MQ mq) throws MqException;

	void asynSendJSON(String exchange, String routingKey, MessageProperties props, MQ mq) throws MqException;
}
