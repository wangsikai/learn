package com.lanking.cloud.component.mq.config;

import java.util.concurrent.Executor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.component.mq.producer.MqSenderImpl;

@Configuration
public class RabbitMqAutoConfiguration {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Bean
	MqSender mqSender(@Qualifier("rabbitMqSendExecutor") Executor rabbitMqSendExecutor) {
		MqSenderImpl mqSender = new MqSenderImpl();
		mqSender.setRabbitTemplate(rabbitTemplate);
		mqSender.setTaskExecutor(rabbitMqSendExecutor);
		return mqSender;
	}

}
